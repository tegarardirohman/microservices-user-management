import com.tegar.auth_service.model.dto.request.AuthRequest;
import com.tegar.auth_service.model.dto.response.LoginResponse;
import com.tegar.auth_service.model.dto.response.SignUpResponse;
import com.tegar.auth_service.model.entity.User;
import com.tegar.auth_service.repository.UserRepository;
import com.tegar.auth_service.service.AuthService;
import com.tegar.auth_service.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public SignUpResponse signup(AuthRequest request) {
        try {
            // Cek jika email sudah terdaftar
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
            }

            // Membuat user baru
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .email(StringUtils.lowerCase(request.getEmail()))
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role("ROLE_USER")
                    .build();

            // Simpan ke database dan Redis
            userRepository.save(user);
            redisTemplate.opsForValue().set("user:" + user.getEmail(), user, 1, TimeUnit.HOURS);

            // Kembalikan response
            return SignUpResponse.builder()
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        } catch (ResponseStatusException e) {
            throw e; // Biarkan ResponseStatusException dilempar
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to sign up", e);
        }
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        try {
            // Cek user di Redis atau database
            Optional<User> userOpt = Optional.ofNullable(
                    redisTemplate.opsForValue().get("user:" + request.getEmail())
            ).map(o -> (User) o).or(() -> userRepository.findByEmail(request.getEmail()));

            // Jika tidak ditemukan
            if (userOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }

            User user = userOpt.get();

            // Cek password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }

            // Simpan ke Redis
            redisTemplate.opsForValue().set("user:" + user.getEmail(), user, 1, TimeUnit.HOURS);

            // Generate token
            String token = jwtUtil.generateToken(user);

            // Kembalikan response
            return LoginResponse.builder()
                    .token(token)
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
        } catch (ResponseStatusException e) {
            throw e; // Biarkan ResponseStatusException dilempar
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to log in", e);
        }
    }
}
