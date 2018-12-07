package com.workbei.controller.autocreate;

import com.workbei.controller.util.ValidateChecker;
import com.workbei.model.view.autocreate.AutoCreateTeamVO;
import com.workbei.util.LogFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import static com.workbei.util.LogFormatter.*;

/**
 * @author Wallace Mao
 * Date: 2018-12-06 17:58
 */
@RestController
@RequestMapping("/v3w/autoCreate")
public class AutoCreateController {
    private static final Logger bizLogger = LoggerFactory.getLogger(AutoCreateController.class);

    private ValidateChecker checker;
    private Validator autoCreateTeamValidator;

    @Autowired
    public AutoCreateController(ValidateChecker checker) {
        this.checker = checker;
    }
    @Autowired
    public void setAutoCreateTeamValidator(Validator autoCreateTeamValidator) {
        this.autoCreateTeamValidator = autoCreateTeamValidator;
    }

    @PostMapping("/team")
    public AutoCreateTeamVO createTeam(
            @RequestHeader("token") String token,
            AutoCreateTeamVO autoCreateTeamVO,
            BindingResult result
    ){
        bizLogger.info(LogFormatter.format(
                LogEvent.START,
                "createTeam",
                getKV("token", token),
                getKV("autoCreateTeamVO", autoCreateTeamVO)
        ));
        checker.check(autoCreateTeamVO, autoCreateTeamValidator, result);

        return autoCreateTeamVO;
    }
}
