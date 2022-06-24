package de.hsrm.mi.web.projekt.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class BackendInfoService {
    Logger logger = LoggerFactory.getLogger(BackendInfoService.class);

    @Autowired
    private SimpMessagingTemplate messaging;

    public void sendInfo(String topicname, BackendOperation operation, long id) {
        messaging.convertAndSend("/topic"+topicname, new BackendInfoMessage(topicname, operation, id));
        logger.info("sendInfo() durchgeführt");
    }
}
