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
                        System.out.println();
                    }

                }
            }
            int count = 0;
            double amount = 100000;
            //首日买600股
            count += 600;
            double buyPrice = (list.get(0).getHigh() - list.get(0).getLow()) * 0.1 + list.get(0).getLow();
            amount -= (buyPrice * 600 + 10);
            float sellFactor = 1.05f;
            float buyFactor = 0.96f;
            LocalDate lastBuyDay = null;
            LocalDate lastSellDay = null;

            DecimalFormat df = new DecimalFormat("#.00");
            System.out.println(list.get(0).getDate() + "以" + df.format(buyPrice) + "买入600股,当前持股" + count + ",当前现金" + df.format(amount)
                    + ",当前总价值" + df.format(amount + list.get(0).getClose() * count));
            for (int i = 1; i < list.size(); i++) {
                //最高比昨日收盘高4%
                LocalDate curDay = LocalDate.parse(list.get(i).getDate(), DateTimeFormatter.ofPattern("yyMMdd"));
                if (lastSellDay != null && curDay.toEpochDay() - lastSellDay.toEpochDay() < 2) {
                    continue;
                }
                if (list.get(i).getHigh() / list.get(i - 1).getClose() > sellFactor && count >= 200) {
                    //判断是否有股票卖
                    count -= 200;
                    double sellPrice = list.get(i - 1).getClose() * (sellFactor - 0.05);
                    amount += (sellPrice * 200 - 10);
                    System.out.println(list.get(i).getDate() + "以" + df.format(sellPrice) + "卖出200股,当前持股" + count + ",当前现金" + df.format(amount)
                            + ",当前总价值" + df.format(amount + list.get(i).getClose() * count));
                    lastSellDay = curDay;
                }//最低比昨日低4%
                else if (list.get(i).getLow() / list.get(i - 1).getClose() < buyFactor) {
                    curDay = LocalDate.parse(list.get(i).getDate(), DateTimeFormatter.ofPattern("yyMMdd"));
                    if (lastBuyDay != null && curDay.toEpochDay() - lastBuyDay.toEpochDay() < 2) {
                        continue;
                    }
                    //判断是否有钱买
                    if (amount >= list.get(i - 1).getClose() * (buyFactor + 0.05) * 200 + 10) {
                        count += 200;
                        buyPrice = list.get(i - 1).getClose() * (buyFactor + 0.05);
                        amount -= (buyPrice * 200 + 10);
                        System.out.println(list.get(i).getDate() + "以" + df.format(buyPrice) + "买入200股,当前持股" + count + ",当前现金" + df.format(amount)
                                + ",当前总价值" + df.format(amount + list.get(i).getClose() * count));
                        lastBuyDay = curDay;
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
