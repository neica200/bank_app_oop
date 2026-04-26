package com.pao.project.banca.model;

import java.util.Objects;
import java.util.UUID;

public class Client implements Comparable<Client>{
    private final String id;
    private String nume;
    private String prenume;
    private String email;

    public Client(String nume, String prenume, String email) {
        this.id = UUID.randomUUID().toString();
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
    }

    public String getId() {
        return id;
    }
    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nume='" + nume + "'" +
                ", prenume='" + prenume + "'" +
                ", email='" + email + "'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int compareTo(Client o) {
        int res = this.nume.compareTo(o.nume);
        if (res == 0) return this.prenume.compareTo(o.prenume);
        return res;
    }
}
