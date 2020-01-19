import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HeXun {

    public void getCsv() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://data.gtimg.cn/flashdata/hushen/daily/19/sz000651.js?visitDstTime=1";
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            System.out.println(response1.getCode());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            String content = EntityUtils.toString(entity1);
            String[] strs = content.split("\\\\n\\\\\n");
            List<StockVo> list = new ArrayList<>();
            for (String temp : strs) {
                if (temp.startsWith("19")) {
                    String[] details = temp.split(" ");
                    if (details.length == 6) {
                        StockVo stockVo = new StockVo();
                        stockVo.setDate(details[0]);
                        stockVo.setOpen(Float.valueOf(details[1]));
                        stockVo.setClose(Float.valueOf(details[2]));
                        stockVo.setHigh(Float.valueOf(details[3]));
                        stockVo.setLow(Float.valueOf(details[4]));
                        list.add(stockVo);
                    }

                }
            }
            int count = 0;
            double amount = 100000;
            //首日买1000股
            count += 2000;
            int buyCount = 200;
            int sellCount = 200;
            double buyPrice = (list.get(0).getHigh() - list.get(0).getLow()) * 0.1 + list.get(0).getLow();
            amount -= (buyPrice * count + 10);
            float sellFactor = 0.9f;
            float buyFactor = 0.9f;
            int netPrice = 35;
            LocalDate lastBuyDay = null;
            LocalDate lastSellDay = null;

            DecimalFormat df = new DecimalFormat("#.00");
            System.out.println(list.get(0).getDate() + "以" + df.format(buyPrice) + "买入1000股,当前持股" + count + ",当前现金" + df.format(amount)
                    + ",当前总价值" + df.format(amount + list.get(0).getClose() * count));
            for (int i = 1; i < list.size(); i++) {
                LocalDate curDay = LocalDate.parse(list.get(i).getDate(), DateTimeFormatter.ofPattern("yyMMdd"));
//                if (lastSellDay != null && curDay.toEpochDay() - lastSellDay.toEpochDay() < 2) {
//                    continue;
//                }
                System.out.println(list.get(i).getDate() + "开盘" + list.get(i).getOpen() + "最高" + list.get(i).getHigh() +"最低" + list.get(i).getLow()
                        + ",收盘" + list.get(i).getClose());
                if (list.get(i).getHigh() > netPrice + 5 && count >= sellCount) {
                    //判断是否有股票卖
                    count -= sellCount;
                    double sellPrice = (list.get(i).getHigh() - list.get(i - 1).getClose()) * (sellFactor) + list.get(i - 1).getClose();
                    amount += (sellPrice * sellCount - 10);

                    System.out.println(list.get(i).getDate() + "以" + df.format(sellPrice) + "卖出" + sellCount +"股,当前持股" + count + ",当前现金" + df.format(amount)
                            + ",当前总价值" + df.format(amount + list.get(i).getClose() * count));
                    netPrice += 5;
                    sellCount += 100;
                    if(buyCount > 100){
                        buyCount -= 100;
                    }
                } else if (list.get(i).getLow() < netPrice - 5
                        && amount >= (list.get(i - 1).getClose() - (list.get(i - 1).getClose() - list.get(i).getLow()) * buyFactor) * buyCount) {
                    buyPrice = list.get(i - 1).getClose() - (list.get(i - 1).getClose() - list.get(i).getLow()) * buyFactor;
                    //判断是否有钱买
                    if (amount >= buyPrice * buyCount + 10) {
                        count += buyCount;
                        amount -= (buyPrice * buyCount + 10);
                        System.out.println(list.get(i).getDate() + "以" + df.format(buyPrice) + "买入" + buyCount + "股,当前持股" + count + ",当前现金" + df.format(amount)
                                + ",当前总价值" + df.format(amount + list.get(i).getClose() * count));
                        netPrice -= 5;
                        if(sellCount > 100){
                            sellCount -= 100;
                        }

                        buyCount += 100;
                    }
                }
            }
            System.out.println("最终持股" + count + ",现金" + df.format(amount) + ";总价值" + df.format((amount + list.get(list.size() - 1).getClose() * count)));

            EntityUtils.consume(entity1);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            response1.close();
        }
    }

    public static void main(String[] args) throws IOException {
        HeXun neteaseCsv = new HeXun();
        neteaseCsv.getCsv();
    }

}
