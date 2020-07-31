package com.phudnguyen.bot.skypebot.feign;

import com.phudnguyen.bot.skypebot.dto.ConversationsResponse;
import com.phudnguyen.bot.skypebot.dto.PostMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "skypeDataClient",
//        url = "https://azcus1-client-s.gateway.messenger.live.com"
        url = "https://azwus1-client-s.gateway.messenger.live.com",
        configuration = FeignClientConfiguration.class)
public interface SkypeDataClient {
    @RequestMapping(method = RequestMethod.GET,
        path = "/v1/users/ME/conversations?view=supportsExtendedHistory%7Cmsnp24Equivalent&startTime=1&targetType=Passport%7CSkype%7CLync%7CThread%7CAgent%7CShortCircuit%7CPSTN%7CFlxt%7CNotificationStream%7CCortanaBot%7CModernBots%7CsecureThreads%7CInviteFree"
        )
    ConversationsResponse getConversation(@RequestParam("pageSize") int pageSize);

    @RequestMapping(method = RequestMethod.POST,
            path = "/v1/users/ME/conversations/{targetId}/messages"
    )
    String postMessage(@PathVariable(name = "targetId") String targetId, @RequestBody PostMessageRequest request);
}
