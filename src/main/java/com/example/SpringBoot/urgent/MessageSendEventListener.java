package com.example.SpringBoot.urgent;

import com.example.SpringBoot.urgent.repository.UrgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageSendEventListener {
    private final UrgentRepository urgentRepository;
    private final MessageSendEvent messageSendEvent;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void formSave(MessageEvent event) {
        String region = event.getRegion();
        List<String> phoneNumList = urgentRepository.findByRegion(region);

        System.out.println("phoneNumList = " + phoneNumList);
        System.out.println("size = " + phoneNumList.size());

        if(phoneNumList != null && phoneNumList.size() > 1){
            messageSendEvent.sendMessages(phoneNumList);
        } else if(phoneNumList != null && phoneNumList.size() == 1) {
            messageSendEvent.sendOneMessage(phoneNumList.get(0));
        } else {
            // 우리 동네 돌보미가 없을 때...
        }
    }
}
