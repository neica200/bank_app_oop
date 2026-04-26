package com.pao.project.banca.model;
import java.util.Objects;

//clasa imutabila
public final class IBAN {
    private final String value;

    public IBAN(String value) {
        if( value == null || value.length() < 10 ) {
            throw new IllegalArgumentException("Format IBAN invalid");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IBAN iban = (IBAN) o;
        return Objects.equals(value,iban.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
