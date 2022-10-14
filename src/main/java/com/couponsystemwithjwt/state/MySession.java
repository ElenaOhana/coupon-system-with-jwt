package com.couponsystemwithjwt.state;

import com.couponsystemwithjwt.services.ClientService;

import java.time.LocalDateTime;
import java.util.Objects;

public class MySession {
    private ClientService clientService;

    private LocalDateTime lastActiveLDT;

    public MySession(ClientService clientService, LocalDateTime lastActiveLDT) {
        this.clientService = clientService;
        this.lastActiveLDT = lastActiveLDT;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public LocalDateTime getLastActiveLDT() {
        return lastActiveLDT;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setLastActiveLDT(LocalDateTime lastActiveLDT) {
        this.lastActiveLDT = lastActiveLDT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MySession)) return false;
        MySession session = (MySession) o;
        return getClientService().equals(session.getClientService()) && getLastActiveLDT().equals(session.getLastActiveLDT());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientService(), getLastActiveLDT());
    }
}
