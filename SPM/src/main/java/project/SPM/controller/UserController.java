package project.SPM.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.SPM.Entity.UserEntity;
import project.SPM.dto.MailDTO;
import project.SPM.dto.UserDTO;
import project.SPM.validator.UserValidator;
import project.SPM.vo.UserVo;
import project.SPM.service.impl.UserService;
import project.SPM.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller("userController")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    // TODO: 2022-05-16 로그인 한 유저에 맞는 테이블이 생성되야 하는데 한 테이블을 모든 유저가 공유하고 있는 큰 문제가 있다. 
    private final UserService userService;
    private final UserValidator userValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder {}", dataBinder);
        dataBinder.addValidators(userValidator);
    }

    // TODO: 2022-06-03  인덱스 페이지 띄우기 - Home Controller 와 관계 해석하기 필요
    // 인덱스 페이지
    @GetMapping("/index")
    private String index(){
        return "redirect:/index";
    }

    // 회원 가입 페이지 - 기본 화면
    @GetMapping("/regUser")
    public String regUserForm(Model model) {

        model.addAttribute("userVo", new UserVo());

        return "user/regUser";
    }

    // 회원 가입 페이지 - 로직 처리
    @PostMapping("/regUser/insert")
    public String InsertRegUser(@Validated @ModelAttribute UserVo userVo,
                                BindingResult bindingResult,
                                Model model){

        try {
            UserDTO userDTO = new UserDTO(
                    userVo.getUserNo(),
                    userVo.getUserName(),
                    userVo.getUserPn(),
                    userVo.getUserEmail(),
                    userVo.getUserId(),
                    userVo.getUserPw(),
                    userVo.getUserAddr()
            );

            userService.InsertUser(userDTO);

            // 서비스에서 아이디 및 이메일 중복 체크 시 Exception을 던지고 처리
        } catch (IllegalArgumentException httpStatusCodeException) {

            log.debug(httpStatusCodeException.getMessage());

            model.addAttribute("msg", httpStatusCodeException.getMessage());
            model.addAttribute("url", "/user/logIn");

        } finally {

            if (bindingResult.hasErrors()) {

                return "user/regUser";
            }

            return  "/user/logIn";
        }

    }

    // 로그인 페이지 - 기본 화면
    @GetMapping("/logIn")
    public String userLogin(Model model) {

        model.addAttribute("userVo", new UserVo());

        return "user/logIn";
    }

    // 로그인 페이지 - 로직 처리
    @PostMapping("/logIn/page")
    public String login(@ModelAttribute UserVo userVo, HttpServletRequest request, HttpSession session) throws Exception {

        // 로그인 전에 세션 삭제
        session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //세션에 있는 정보가 싹 날라간다.
        }

        UserDTO userDTO = new UserDTO(
                userVo.getUserNo(),
                userVo.getUserName(),
                userVo.getUserPn(),
                userVo.getUserEmail(),
                userVo.getUserId(),
                userVo.getUserPw(),
                userVo.getUserAddr()
        );

        boolean res = userService.login(userDTO);

        if (res == true) {
            UserEntity dto = userService.loginSession(userDTO);
            session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, dto);
        } else {
            return "user/login";
        }

        return "redirect:/user/index";
    }

    // 로그아웃 로직 처리
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpSession session) {

        session.invalidate();

        return "redirect:/user/logIn";
    }

    @GetMapping("/findPw")
    public String changePwPage() {

        return "user/findPw";
    }

    @PostMapping("/findPw")
    public String changePw(HttpServletRequest request) throws Exception {

        String userEmail = request.getParameter("address");
        log.debug("### request.getParameter : {}", request.getParameter("address")); //t
        log.debug("### userEmail : {}", userEmail); //t

        MailDTO mailDTO = userService.findPw(userEmail);
        log.debug("### mailDTO : {}", mailDTO);

        userService.sendMail(mailDTO);

        return "redirect:/logIn";
    }
}
