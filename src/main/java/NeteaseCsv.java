import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.SessionOutputBuffer;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NeteaseCsv {

    public void getCsv() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://quotes.money.163.com/service/chddata.html?code=0000300&start=20151219&end=20171108&fields=TCLOSE;HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;VOTURNOVER";
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {
            System.out.println(response1.getCode());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            String content = EntityUtils.toString(entity1);
            FileWriter fileWriter = new FileWriter("D:/file.csv");
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            EntityUtils.consume(entity1);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            response1.close();
        }
    }

    public static void main(String[] args) throws IOException {
        NeteaseCsv neteaseCsv = new NeteaseCsv();
        neteaseCsv.getCsv();
    }

}
