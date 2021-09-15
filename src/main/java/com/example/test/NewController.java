package com.example.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class NewController {
    private static final String SESSION_ID = "m5btai6h6p19722vtrpp6uc6u0";

    @GetMapping("/test")
    public ResponseEntity<Void> tryTicket() throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of("coup.txt"), StandardCharsets.UTF_8));

        for (int i = 0; i < fileContent.size(); i++) {
            if (sendIce("%5B%22" + fileContent.get(i).trim() + "%22%5D")) {
                fileContent.set(i, fileContent.get(i).trim() + " ice");
            } else if (sendCola("%5B%22" + fileContent.get(i).trim() + "%22%5D")) {
                fileContent.set(i, fileContent.get(i).trim() + " cola");
            } else if (sendSladko("%5B%22" + fileContent.get(i).trim() + "%22%5D")) {
                fileContent.set(i, fileContent.get(i).trim() + " sladko");
            } else
                fileContent.set(i, fileContent.get(i).trim() + " --------------");
        }

        Files.write(Path.of("coup.txt"), fileContent, StandardCharsets.UTF_8);

        return new ResponseEntity<>(NO_CONTENT);
    }

    public boolean sendIce(String headerCoupons) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=" + SESSION_ID + "; _gcl_au=1.1.1590048803.1631268838; _ga=GA1.2.2122116502.1631268838; _fbp=fb.1.1631268838810.12867522; MicrosoftApplicationsTelemetryDeviceId=9912ef75-accf-4089-98fb-065af68242aa; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-10T10:14:00.888Z; __zlcmid=160kJwIPYQjSGut; _gid=GA1.2.1909312768.1631617407; Customer_Dominos=000ad654d59f93352d6a41d8aad81fc35bffe40b4f472ecadaad9967adc77eb28780488bc3b590dd3fcf41b4fbf585700f007e49e695502b068370c7b17a4095; __utma=171614261.2122116502.1631268838.1631646333.1631646333.1; __utmc=171614261; __utmz=171614261.1631646333.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); KeepAlive=7baed2ea0b4f1e54ce21d202f1efcbef1b1d32c93a74b6d2cc4f19f19f3103834626a78605e95d084a9663126b4e924ae0598ec1459a6c8357b2bbfec1c19e9b; Order_data=%7B%22store_id%22%3A%2254%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2211%3A20%22%2C%22store_perma%22%3A%22sofia-mladost%22%7D; Toppings=%5B%7B%22product%22%3A%22799%22%2C%22toppings%22%3A%5B%7B%2217%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%2C%22197%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2249751%22%5D%7D%5D; Basket=%7B%2254%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2249751%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%2217%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%2C%22197%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%2C%7B%22ids%22%3A%5B%2246487%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
                .header("origin", "https://www.dominos.bg")
                .header("referer", "https://www.dominos.bg/checkout")
                .header("sec-ch-ua", "Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .data("address_id", "0")
                .data("time", "17:50")
                .data("time_type", "LATER")
                .data("store_id", "54")
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();


        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("green-border");

        return eles.size() != 0;
    }

    public boolean sendCola(String headerCoupons) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=" + SESSION_ID + "; _gcl_au=1.1.1590048803.1631268838; _ga=GA1.2.2122116502.1631268838; _fbp=fb.1.1631268838810.12867522; MicrosoftApplicationsTelemetryDeviceId=9912ef75-accf-4089-98fb-065af68242aa; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-10T10:14:00.888Z; __zlcmid=160kJwIPYQjSGut; _gid=GA1.2.1909312768.1631617407; Customer_Dominos=000ad654d59f93352d6a41d8aad81fc35bffe40b4f472ecadaad9967adc77eb28780488bc3b590dd3fcf41b4fbf585700f007e49e695502b068370c7b17a4095; __utma=171614261.2122116502.1631268838.1631646333.1631646333.1; __utmc=171614261; __utmz=171614261.1631646333.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); KeepAlive=7baed2ea0b4f1e54ce21d202f1efcbef1b1d32c93a74b6d2cc4f19f19f3103834626a78605e95d084a9663126b4e924ae0598ec1459a6c8357b2bbfec1c19e9b; Order_data=%7B%22store_id%22%3A%2254%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2211%3A20%22%2C%22store_perma%22%3A%22sofia-mladost%22%7D; Toppings=%5B%7B%22product%22%3A%22799%22%2C%22toppings%22%3A%5B%7B%2217%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%2C%22197%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2249751%22%5D%7D%5D; Basket=%7B%2254%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2249751%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%2217%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%2C%22197%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%2C%7B%22ids%22%3A%5B%2246487%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
                .header("origin", "https://www.dominos.bg")
                .header("referer", "https://www.dominos.bg/checkout")
                .header("sec-ch-ua", "Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .data("address_id", "0")
                .data("time", "17:50")
                .data("time_type", "LATER")
                .data("store_id", "54")
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();

        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("green-border");

        return eles.size() != 0;
    }

    public boolean sendSladko(String headerCoupons) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=" + SESSION_ID + "; _gcl_au=1.1.1590048803.1631268838; _ga=GA1.2.2122116502.1631268838; _fbp=fb.1.1631268838810.12867522; MicrosoftApplicationsTelemetryDeviceId=9912ef75-accf-4089-98fb-065af68242aa; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-10T10:14:00.888Z; __zlcmid=160kJwIPYQjSGut; _gid=GA1.2.1909312768.1631617407; Customer_Dominos=000ad654d59f93352d6a41d8aad81fc35bffe40b4f472ecadaad9967adc77eb28780488bc3b590dd3fcf41b4fbf585700f007e49e695502b068370c7b17a4095; __utma=171614261.2122116502.1631268838.1631646333.1631646333.1; __utmc=171614261; __utmz=171614261.1631646333.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); KeepAlive=7baed2ea0b4f1e54ce21d202f1efcbef1b1d32c93a74b6d2cc4f19f19f3103834626a78605e95d084a9663126b4e924ae0598ec1459a6c8357b2bbfec1c19e9b; Order_data=%7B%22store_id%22%3A%2254%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2211%3A20%22%2C%22store_perma%22%3A%22sofia-mladost%22%7D; Toppings=%5B%7B%22product%22%3A%22799%22%2C%22toppings%22%3A%5B%7B%2217%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%2C%22197%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2249751%22%5D%7D%5D; Basket=%7B%2254%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2249751%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%2217%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%2C%22197%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%2C%7B%22ids%22%3A%5B%2246487%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
                .header("origin", "https://www.dominos.bg")
                .header("referer", "https://www.dominos.bg/checkout")
                .header("sec-ch-ua", "Chromium\";v=\"92\", \" Not A;Brand\";v=\"99\", \"Google Chrome\";v=\"92")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .data("address_id", "0")
                .data("time", "17:50")
                .data("time_type", "LATER")
                .data("store_id", "54")
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();

        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("green-border");

        return eles.size() != 0;
    }
}
