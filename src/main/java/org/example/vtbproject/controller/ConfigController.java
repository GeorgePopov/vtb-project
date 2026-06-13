package org.example.vtbproject.controller;


import org.example.vtbproject.model.ActivateConfig;
import org.example.vtbproject.model.DelayConfig;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

    private final DelayConfig delayConfig;
    private final ActivateConfig activateConfig;

    public ConfigController(DelayConfig delayConfig, ActivateConfig activateConfig) {
        this.delayConfig = delayConfig;
        this.activateConfig = activateConfig;
    }

    @GetMapping("/delay")
    public int getCurrentDealy() {
        return delayConfig.getResponseDealy();
    }

    @PostMapping("/delay")
    public String updateResponseDealy(@RequestBody int ms) {
        delayConfig.setResponseDelay(ms);
        return "Вреся отклика " + ms + "ms";
    }

    @PostMapping("/activate")
    public String updateActivateConfig(@RequestBody boolean activate) {
        activateConfig.setActivate(activate);
        if (activate) {
            return "Приложение переведено в режим эмуляции ошибки 503 (Недоступно)";
        } else {
            return "Приложение снова доступно и работает в штатном режиме";
        }
    }
}