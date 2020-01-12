import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class XueQiu {

    public void getRes() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        LocalDateTime start = LocalDateTime.of(2019, 01, 01, 0, 0);
        LocalDateTime end = LocalDateTime.of(2019, 01, 11, 0, 0);
        String url = "https://xueqiu.com/stock/forchartk/stocklist.json?symbol=SH600756&period=1day&type=before&begin=" +
                start.toInstant(ZoneOffset.of("+8")).toEpochMilli() +
                "&end=" +
                end.toInstant(ZoneOffset.of("+8")).toEpochMilli() +
                "&_=" +
                LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie","device_id=cb1c2031b1c36f7752e0517fe1784df2; aliyungf_tc=AQAAANJN/Q+pqAUAwUNJ3zRbPTxobwA3; acw_tc=2760824015782334353896603e2041f0b0e83c71746261d80cd4d7547d0f9a; xq_a_token.sig=TIigUHtW6VQxXvSEOwIOURxn6V0; xq_r_token.sig=bS2mSsfHTJcugqefE9LcNGpCqZ8; xqat.sig=mPd6Qjk06dbsOgDQMdBGs5bcwwc; _ga=GA1.2.262447627.1578233440; s=dg1ghfyhtm; __utma=1.262447627.1578233440.1578233627.1578233627.1; __utmc=1; __utmz=1.1578233627.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); Hm_lvt_1db88642e346389874251b5a1eded6e3=1578233436,1578233650,1578233657,1578409357; _gid=GA1.2.1693898682.1578834410; remember=1; xq_a_token=2e9a64b70b060a4e1219c700669aad9b5ad83aa7; xqat=2e9a64b70b060a4e1219c700669aad9b5ad83aa7; xq_r_token=84584ad72c4ae46421175758956fc0f28484ff34; xq_is_login=1; u=3248381339; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1578835168");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            System.out.println(response1.getCode());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            String content = EntityUtils.toString(entity1);
            System.out.println("content");
            System.out.println(content);
//            FileWriter fileWriter = new FileWriter("D:/file.csv");
//            fileWriter.write(content);
//            fileWriter.flush();
//            fileWriter.close();
            EntityUtils.consume(entity1);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            response1.close();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println( LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        XueQiu xueQiu = new XueQiu();
        xueQiu.getRes();
    }

}
