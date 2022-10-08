package demo.controller;

import demo.dvo.FakeProducerDTO;
import demo.service.ReactiveProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("producer")
public class ProducerController {

    @Autowired
    private ReactiveProducerService reactiveProducerService;

    @GetMapping
    public void sendMessage() {
        FakeProducerDTO fakeProducerDTO = new FakeProducerDTO("muhammed");
        this.reactiveProducerService.send(fakeProducerDTO);
    }
}
