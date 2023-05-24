package com.couponsystemwithjwt.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.couponsystemwithjwt.entity_beans.Administrator;
import com.couponsystemwithjwt.entity_beans.Company;
import com.couponsystemwithjwt.entity_beans.Customer;
import com.couponsystemwithjwt.exceptions.CouponSystemException;
import com.couponsystemwithjwt.exceptions.ErrMsg;
import com.couponsystemwithjwt.services.ClientService;
import com.couponsystemwithjwt.state.MySession;
import com.couponsystemwithjwt.types.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Service
@Transactional
public class TokenManager {

    @Autowired
    private HashMap<Long, MySession> sessions;

    public TokenManager(HashMap<Long, MySession> sessions) {
        this.sessions = sessions;
    }

    public String createTokenForAdmin(Administrator admin){
        String token = JWT.create()
                .withIssuer("ElenaTech LTD")
                .withIssuedAt(new Date())
                .withClaim("id", admin.getId())
                .withClaim("email", admin.getEmail())
                .withClaim("clientType", String.valueOf(ClientType.ADMINISTRATOR))
                .sign(Algorithm.HMAC256("topsecretkey"));
        return token;
    }

    public String createTokenForCompany(Company company){
        String token = JWT.create()
                .withIssuer("ElenaTech LTD")
                .withIssuedAt(new Date())
                .withClaim("id", company.getId())
                .withClaim("name", company.getName())
                .withClaim("email", company.getEmail())
                .withClaim("clientType", String.valueOf(ClientType.COMPANY))
                .sign(Algorithm.HMAC256("topsecretkey"));
        return token;
    }

    public String createTokenForCustomer(Customer customer){
        String token = JWT.create()
                .withIssuer("ElenaTech LTD")
                .withIssuedAt(new Date())
                .withClaim("id", customer.getId())
                .withClaim("firstName", customer.getFirstName())
                .withClaim("lastName", customer.getLastName())
                .withClaim("email", customer.getEmail())
                .withClaim("clientType", String.valueOf(ClientType.CUSTOMER))
                .sign(Algorithm.HMAC256("topsecretkey"));
        return token;
    }

    public String returnPureToken(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        if (token.startsWith("Bearer ")) {
            token = request.getHeader("authorization").split(" ")[1]; // When the Token with Bearer: "Bearer jhffdhgfjdhvjfdhgjfgbjhfg..."
        }
        return token;
    }

    /* Deletes expired sessions, will run every minute. Will remove sessions that have existed longer than 30 minutes. */
    @Scheduled(fixedRate = 1000 * 60)
    public void deleteExpired(){
        sessions.values().removeIf(mySession -> mySession.getLastActiveLDT().isAfter(LocalDateTime.now().minusMinutes(30)));
        //sessions.entrySet().removeIf(info -> info.getValue().getLastActiveLDT().isAfter(LocalDateTime.now().minusMinutes(30)));
    }

    public ClientService getClientFromSessionByTokenIdAndSetLastActive(long id) throws CouponSystemException {
        MySession session = sessions.get(id);
        ClientService clientServiceFromSession;
        if (session != null) {
            clientServiceFromSession = session.getClientService();
            session.setLastActiveLDT(LocalDateTime.now());
            sessions.put(id, session);
            return clientServiceFromSession;
        }
        throw new CouponSystemException(ErrMsg.SESSION_NOT_FOUND);
    }
}
