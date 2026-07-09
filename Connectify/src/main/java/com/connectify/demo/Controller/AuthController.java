package com.connectify.demo.Controller;

import com.connectify.demo.Dto.SignupRequest;
import com.connectify.demo.Model.ErrorResponse;
import com.connectify.demo.Model.UserInfo;
import com.connectify.demo.Repository.UserInfoRepository;
import com.connectify.demo.Security.JwtRequest;
import com.connectify.demo.Security.JwtResponse;
import com.connectify.demo.Security.JwtUtil;
import com.connectify.demo.ServiceImpl.UserInfoServiceImpl;
import com.connectify.demo.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static Utility.ConstantUtil.INVALID_CREDENTIAL;


@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final UserInfoService userInfoService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserInfoRepository userInfoRepository;

    @PostMapping("/signup")
    public ResponseEntity<UserInfo> Signup( @Valid @RequestBody SignupRequest signupRequest) {
        try {
            if (signupRequest.getPassword() == null || signupRequest.getUsername() == null) {
                throw new RuntimeException(INVALID_CREDENTIAL);

            }
            if (signupRequest.getPassword().equals(" ") || signupRequest.getUsername().equals(" ") || signupRequest.getPassword().isEmpty() || signupRequest.getUsername().isEmpty()) {
                throw new RuntimeException(INVALID_CREDENTIAL);
            }
            if (signupRequest.getRoles() == null) {
                signupRequest.setRoles("User");
            }
            UserInfo newUser = userInfoService.AddUser(signupRequest);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/error")
    public ResponseEntity<ErrorResponse> error(HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Invalid username and password or your token is expired");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(INVALID_CREDENTIAL);
        }

        UserInfo userDetails = userInfoRepository.findByUsername(request.getUsername());
        String token = this.jwtUtil.generateToken(userDetails.getUsername(),userDetails.getId());
        JwtResponse response = new JwtResponse(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile/{Id}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long Id) {
        UserInfo userInfo = userInfoService.getUserbyId(Id);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserInfo>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                userInfoService.getAllUser(
                        page,
                        size
                )
        );
    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        String mess = userInfoService.deleteUserById(userId);
        return new ResponseEntity<>(mess, HttpStatus.OK);
    }

    @PatchMapping("/update-user/{userId}")
    public ResponseEntity<?> updateUserByUserId(@RequestBody UserInfo userInfo, @PathVariable Long userId) {
        UserInfo updatedUser = userInfoService.updatedUser(userInfo, userId);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

