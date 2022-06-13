package mvc.controller;

import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class CurrencyController {
    @RequestMapping(method = RequestMethod.GET, value = "/{currency}")
    @ResponseBody
    public ResponseEntity<?> getCurrencyByValue(HttpServletRequest request, HttpServletResponse response, @PathVariable String currency){
        List<String> currencyList = new ArrayList<>(Arrays.asList("CNY","USD","AUD","ZAR","NZD","EUR","HKD","JPY","MXN","CAD","CHF","GBP","SEK","SGD","THB"));
        if(!currencyList.contains(currency)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
            RequestBody body = RequestBody.create(mediaType, "{data:{\"Currency\":\""+ currency +"\",\"Currencytype\":\"1\",\"Rangetype\":\"3\",\"Startdate\":null,\"Enddate\":null,\"CurrencyTitle\":\"美元(USD)\"}}");
            Request requestFromBank = new Request.Builder()
                    .url("https://www.esunbank.com.tw/bank/Layouts/esunbank/Deposit/DpService.aspx/GetLineChartJson")
                    .method("POST", body)
                    .addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                    .addHeader("Accept-Language", "en-US,en;q=0.9,zh-TW;q=0.8,zh;q=0.7")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .addHeader("Cookie", "SC_ANALYTICS_GLOBAL_COOKIE=da4dbf1081714fb1ae501d95647c9745|False; ASP.NET_SessionId=uiqjimkn3rpqts0llixwmobp; TS0147d1ae=01033cabac791a8e3909c440ebb8220a5685dd93bada3545f6e70cf5da01accbaa96146f22215f5cfc5f048965389a7141f5775e08; _ga=GA1.3.527717240.1654979941; _gid=GA1.3.919383936.1654979941; _fbp=fb.2.1654979941420.1488724773; _ga_56KQZGV7P0=GS1.1.1654979941.1.0.1654979949.52; ESB-new_Cookie=2902593708.47873.0000; _gat_UA-1422809-1=1; TS0147d1ae=01033cabac5e2a8532fe2253b170b423ba5151315ac458de584ae853b5f1c4e46a22742e3eb8a41403eb4cb62449575c604eb69315")
                    .addHeader("Origin", "https://www.esunbank.com.tw")
                    .addHeader("Referer", "https://www.esunbank.com.tw/bank/personal/deposit/rate/forex/exchange-rate-chart?Currency=USD/TWD&dev=mobile")
                    .addHeader("Sec-Fetch-Dest", "empty")
                    .addHeader("Sec-Fetch-Mode", "cors")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.5005.63 Mobile Safari/537.36")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102\"")
                    .addHeader("sec-ch-ua-mobile", "?1")
                    .addHeader("sec-ch-ua-platform", "\"Android\"")
                    .build();
            try {
                Response responseFromBank = client.newCall(requestFromBank).execute();
                String results = responseFromBank.body().string();

                System.out.println(results);
                return new ResponseEntity<String>(results, HttpStatus.OK);

            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }
}
