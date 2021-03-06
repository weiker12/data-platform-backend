package com.peilian.dataplatform.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peilian.dataplatform.config.GlobalVar;
import com.peilian.dataplatform.util.MyJwt;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fable on 17/3/3.
 */
@Log4j2
public class JwtHelper {

    private static Claims genStatusClaims(int status) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("status", status);
        return Jwts.claims(claimsMap);
    }


    private static Claims parseJwt(String jsonWebToken, byte[] keybytes) {
        Claims claims = null;
        if (jsonWebToken == null || jsonWebToken.length() < 64) {
            return genStatusClaims(-1);
        }
        if (keybytes == null || keybytes.length < 16) {
            return genStatusClaims(-3);
        }
        try {
            claims = Jwts.parser()
                    .setSigningKey(keybytes)
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (MalformedJwtException | SignatureException e) {
            return genStatusClaims(-1);
        } catch (ExpiredJwtException e1) {
            return genStatusClaims(-2);
        } catch (Exception ex) {
            return genStatusClaims(-4);
        }
        if (claims != null) {
            claims.put("status", 0);
            return claims;
        }
        return genStatusClaims(-4);
    }

    private static Claims parseJwt(String jsonWebToken, String base64Security) {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    public static MyJwt parseMyJwt(String jsonWebToken, byte[] keybytes) {
        Claims claims = parseJwt(jsonWebToken, keybytes);
        log.info("parseMyJwt claims:{}", claims);
        MyJwt myJwt = new MyJwt();
        if (claims == null) {
            myJwt.setStatusMsg("????????????");
            return myJwt;
        } else {
            if (claims.containsKey("status")) {
                int status = (int) claims.get("status");
                myJwt.setStatus(status);
                //status //???????????? 0?????? -1?????? -2????????? -3jwt???????????? -4?????????
                switch (status) {
                    case 0:
                        myJwt.setStatusMsg(null);
                        break;
                    case -1:
                        myJwt.setStatusMsg("JWT??????");
                        return myJwt;
                    case -2:
                        myJwt.setStatusMsg("JWT?????????");
                        return myJwt;
                    case -3:
                        myJwt.setStatusMsg("JWT??????????????????");
                        return myJwt;
                    case -4:
                        myJwt.setStatusMsg("??????????????????");
                        return myJwt;
                    default:
                        myJwt.setStatusMsg("????????????");
                        return myJwt;
                }
            }
        }
        myJwt.setCellphone((String) claims.get(GlobalVar.CELLPHONE));
        myJwt.setIss(claims.getIssuer());
        myJwt.setAud(claims.getAudience());
        myJwt.setJti(claims.getId());
        myJwt.setSub(claims.getSubject());
        myJwt.setExp(claims.getExpiration().getTime() / 1000);
        return myJwt;
    }


    public static String reNewJwt(String oldJwt, byte[] keybytes, int ttls) {
        MyJwt myJwt = parseMyJwt(oldJwt, keybytes);
        if (myJwt == null) {
            return null;
        }
        return createMyJwt(myJwt, keybytes);
    }

    public static MyJwt parseMyJwt(String jsonWebToken, String base64Security) {
        if (base64Security == null || base64Security.length() < 4) {
            return null;
        }
        byte[] keybytes = DatatypeConverter.parseBase64Binary(base64Security);
        return parseMyJwt(jsonWebToken, keybytes);
    }

    public static String createMyJwt(MyJwt myJwt, byte[] keybytes, int ttls) {
        if (myJwt == null) {
            return null;
        }
        if (keybytes == null || keybytes.length < 8) {
            return null;
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key signingKey = new SecretKeySpec(keybytes, signatureAlgorithm.getJcaName());
        //??????Token????????????
        ttls = GlobalVar.TTLS_VALUE;
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setId(myJwt.getJti())
                .setIssuer(myJwt.getIss())
                .setAudience(myJwt.getAud())
                .setSubject(myJwt.getSub())
                .claim(GlobalVar.CELLPHONE, myJwt.getCellphone())
                .signWith(signatureAlgorithm, signingKey);
        long expMillis = nowMillis + GlobalVar.TTLS_VALUE * 1000L;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);
        //??????JWT
        return builder.compact();
    }

    public static String createMyJwt(MyJwt myJwt, byte[] keybytes) {
        if (myJwt == null) {
            return null;
        }
        if (keybytes == null || keybytes.length < 8) {
            return null;
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //??????????????????
        Key signingKey = new SecretKeySpec(keybytes, signatureAlgorithm.getJcaName());
        //????????????JWT?????????
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setId(myJwt.getJti())
                .setIssuer(myJwt.getIss())
                .setAudience(myJwt.getAud())
                .setSubject(myJwt.getSub())
                .claim(GlobalVar.CELLPHONE, myJwt.getCellphone())
                .signWith(signatureAlgorithm, signingKey);
        //??????Token????????????
        long expMillis = nowMillis + GlobalVar.TTLS_VALUE * 1000L;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);
        //??????JWT
        return builder.compact();
    }

    public static String createMyJwt(MyJwt myJwt, String base64Security) {
        if (base64Security == null || base64Security.length() < 4) {
            return null;
        }
        byte[] keybytes = Base64.getDecoder().decode(base64Security);
        return createMyJwt(myJwt, keybytes);
    }

    public static String getJwtStrFromAuthorization(String auth) {
        if (auth == null || auth.length() < 32) {
            return null;
        }
        if (!"Bearer".equalsIgnoreCase(auth.substring(0, 6))) {
            return null;
        }
        return auth.substring(7, auth.length());
    }

    public static MyJwt getMyJwtFromStringWithoutAuth(String jwtStr) {
        if (jwtStr == null || jwtStr.length() < 32) {
            return null;
        }
        String[] jwtArray = jwtStr.split("\\.");
        if (jwtArray.length != 3) {
            return null;
        }
        String preload = jwtArray[1];
        if (preload.length() < 12) {
            return null;
        }
        byte[] jsonBytes = Base64.getDecoder().decode(preload);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonBytes, MyJwt.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static MyJwt getMyJwtFromHttpHeaderWithoutAuth(HttpServletRequest req, String jwtHeader) {
        String auth = req.getHeader(jwtHeader);
        if ((auth == null) || (auth.length() < 32)) {
            return null;
        }
        return getMyJwtFromStringWithoutAuth(auth);
    }

    public static MyJwt getMyJwtFromHttpHeaderWithoutAuth(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if ((auth == null) || (auth.length() < 32)) {
            return null;
        }
        String jwtStr = getJwtStrFromAuthorization(auth);
        if (jwtStr == null) {
            return null;
        }
        return getMyJwtFromStringWithoutAuth(jwtStr);
    }
}


