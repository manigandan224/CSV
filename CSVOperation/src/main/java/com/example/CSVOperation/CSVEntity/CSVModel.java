package com.example.CSVOperation.CSVEntity;

import javax.persistence.*;

@Entity
@Table(name="USERDETAILS")
public class CSVModel {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="Name")
    private String name;
    @Column(name="Address")
    private String address;
    @Column(name="Email")
    private String email;
    public CSVModel()
    {

    }
    public CSVModel(String name,String address,String email)
    {
        super();
        this.name=name;
        this.address=address;
        this.email=email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
