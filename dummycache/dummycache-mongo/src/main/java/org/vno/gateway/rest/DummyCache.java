package org.vno.gateway.rest;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vno.gateway.vault.CacheEntryRepository;
import org.vno.gateway.vault.CacheVault;
import org.vno.gateway.vault.MongoDBVault;

@RequestMapping("/")
@RestController
@PreAuthorize("isAuthenticated()")
public class DummyCache {

    @Value( "${real.server.url}" )
    private String realServerUrl;

    @Autowired
    private CacheEntryRepository cerVault;

    private CacheVault cachedResponses = null;

    @GetMapping("/**")
    ResponseEntity<?> dummyCacheGetRq(
                            @RequestParam Map<String, String> originalParameters,
                            HttpServletRequest originalRequest,
                            HttpServletResponse originalResponse) {

        if (cachedResponses == null) {
           cachedResponses = new MongoDBVault(cerVault);
        }

        String cachedResponse = cachedResponses.get(originalRequest.getRequestURI());

        if (cachedResponse != null) {
            System.out.println("Answer for GET " + originalRequest.getRequestURI() + " found in cache:");
            System.out.println(cachedResponse);
            return ResponseEntity.ok(cachedResponse);
        }

        String realServerURL = realServerUrl + originalRequest.getRequestURI();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(realServerURL);

        System.out.println("=========");
        System.out.println("GET " + originalRequest.getRequestURI());
        System.out.println("====");
        System.out.println("Headers: ");
        for (Enumeration names = originalRequest.getHeaderNames(); names.hasMoreElements();) {
            String name = (String)names.nextElement();
            for (Enumeration values = originalRequest.getHeaders(name); values.hasMoreElements();) {
                String value = (String)values.nextElement();
                System.out.println(name + " : " + value);
                request.addHeader(name, value);
            }
        }
        System.out.println("====");
        try {
            System.out.println("Asking " + realServerURL);

            HttpResponse response = client.execute(request);

            System.out.println("Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());
            System.out.println("=========");

            if (response.getStatusLine().getStatusCode() == 200) {
                cachedResponses.put(originalRequest.getRequestURI(), result.toString());
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity<String>(result.toString(),
                        HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
            }

        } catch (IOException ex) {
            return new ResponseEntity<String>(
                    "Unable to reach the remote server " + realServerURL,
                    HttpStatus.BAD_GATEWAY);
        }

    }

    @PutMapping("/**")
    ResponseEntity<?> dummyCachePutRq(
            @RequestParam Map<String, String> originalParameters,
            @RequestBody  String originalBody,
            HttpServletRequest originalRequest,
            HttpServletResponse originalResponse) {

        String realServerURL = realServerUrl + originalRequest.getRequestURI();
        HttpClient client = new DefaultHttpClient();
        HttpPut request = new HttpPut(realServerURL);

        System.out.println("=========");
        System.out.println("PUT " + originalRequest.getRequestURI());
        System.out.println("====");
        System.out.println("Headers: ");
        for (Enumeration names = originalRequest.getHeaderNames(); names.hasMoreElements();) {
            String name = (String)names.nextElement();
            for (Enumeration values = originalRequest.getHeaders(name); values.hasMoreElements();) {
                String value = (String)values.nextElement();
                System.out.println(name + " : " + value);
                if (!name.equals("Content-Length")) {
                    request.addHeader(name, value);
                }
            }
        }
        System.out.println("====");
        System.out.println("Body: ");
        System.out.println(originalBody);
        request.setEntity(new StringEntity(originalBody, "utf-8"));
        System.out.println("====");
        try {
            System.out.println("Asking " + realServerURL);

            HttpResponse response = client.execute(request);

            System.out.println("Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());
            System.out.println("=========");

            if (response.getStatusLine().getStatusCode() == 200) {
                return ResponseEntity.ok(result);
            } else {
                return new ResponseEntity<String>(result.toString(),
                        HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return new ResponseEntity<String>(
                    "Unable to reach the remote server " + realServerURL,
                    HttpStatus.BAD_GATEWAY);
        }
    }
}
