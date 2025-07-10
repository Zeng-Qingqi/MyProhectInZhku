package com.zhku.roomate.service;

import com.zhku.roomate.model.domain.ResetCode;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ResetCodeService {
    // 内存存储验证码，实际生产环境建议用Redis
    private final Map<String, ResetCode> codeStore = new HashMap<>();
    
    // 验证码有效期5分钟
    private static final long EXPIRE_TIME = 1 * 60 * 1000;

    public String generateCode(String email) {
        // 生成6位随机数字验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        
        ResetCode resetCode = new ResetCode();
        resetCode.setEmail(email);
        resetCode.setCode(code);
        resetCode.setExpireTime(new Date(System.currentTimeMillis() + EXPIRE_TIME));
        
        codeStore.put(email, resetCode);
        return code;
    }

    public boolean validateCode(String email, String code) {
        ResetCode resetCode = codeStore.get(email);
        if (resetCode == null) return false;
        
        return resetCode.getCode().equals(code) && 
               resetCode.getExpireTime().after(new Date());
    }

    public void clearCode(String email) {
        codeStore.remove(email);
    }
}