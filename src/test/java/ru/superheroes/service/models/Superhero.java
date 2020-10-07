package ru.superheroes.service.models;

import org.apache.commons.lang3.RandomStringUtils;
import ru.util.CollectionUtil;
import java.time.LocalDate;
import java.util.*;

public class Superhero {

    private int id;
    private String city;
    private String fullName;
    private String mainSkill;
    private Gender gender;
    private String phone;
    private LocalDate birthDate;

    public Integer getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return fullName;
    }


    public String getMainSkill() {
        return mainSkill;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMainSkill(String mainSkill) {
        this.mainSkill = mainSkill;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Superhero()
    {

    }

    public Superhero(Integer id, String city, String fullName, String mainSkill, Gender gender, String phone, LocalDate birthDate)
    {
        this.id = id;
        this.city = city;
        this.fullName = fullName;
        this.mainSkill = mainSkill;
        this.gender = gender;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Superhero RandomSuperhero()
    {
        this.city = RandomStringUtils.randomAlphabetic(10);
        this.fullName = String.format("%s %s %s", RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));
        this.mainSkill = RandomStringUtils.randomAlphabetic(15);
        this.gender = Gender.getRandom();
        this.phone = String.format("+7%s",RandomStringUtils.randomNumeric(10));
        this.birthDate = CollectionUtil.getRandomDate();
        return this;
    }

    public Map<String, Object> getPlayerJson()
    {
        Map<String, Object> playerJson = new HashMap<>();
        playerJson.put("birthDate", getBirthDate().toString());
        playerJson.put("city",getCity());
        playerJson.put("fullName",getFullName());
        playerJson.put("gender",getGender().genderString);
        playerJson.put("mainSkill",getMainSkill());
        playerJson.put("phone",getPhone());
        return playerJson;
    }

    public enum Gender {
        FEMALE ("F"),
        MALE ("M");

        public String genderString;

        Gender(String genderString) {
            this.genderString = genderString;
        }

        public static Gender getRandom()
        {
            return CollectionUtil.getRandomElement(values());
        }
    }
}
