package com.phudnguyen.bot.skypebot.feign;

import com.phudnguyen.bot.skypebot.dto.CreateObjectRequest;
import com.phudnguyen.bot.skypebot.dto.UploadObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "skypeMediaClient",
        url = "https://api.asm.skype.com",
        configuration = FeignClientConfiguration.class)
public interface SkypeMediaClient {
    @RequestMapping(method = RequestMethod.POST,
            path = "/v1/objects",
            headers = "Content-Type=application/json")
    UploadObjectResponse createObject(@RequestBody CreateObjectRequest request,
                                      @RequestHeader("TransactionId") String transactionId);


    @RequestMapping(method = RequestMethod.PUT,
            path = "/v1/objects/{objectId}/content/imgpsh",
            headers = "Content-Type=application")
    String uploadObject(@PathVariable("objectId") String objectId,
                        @RequestHeader("TransactionId") String transactionId,
                        @RequestBody byte[] data);
}
