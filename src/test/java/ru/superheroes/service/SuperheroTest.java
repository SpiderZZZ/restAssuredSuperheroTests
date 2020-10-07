package ru.superheroes.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import ru.superheroes.service.models.Superhero;
import ru.util.CollectionUtil;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class SuperheroTest {

    private static final String getSuperheroes = "/superheroes";
    private static final String createSuperhero = "/superheroes";
    private static final String getSuperhero = "/superheroes/%s";
    private static final String updateSuperhero = "/superheroes/%s";
    private static final String deleteSuperhero = "/superheroes/%s";
    private static final Superhero superhero = new Superhero().RandomSuperhero();
    private List<Integer> listSuperheroesId;

    @BeforeClass
    public void setUp() throws Exception {
        RestAssured.baseURI = "https://superhero.qa-test.csssr.com/";
        RestAssured.port = 443;
        listSuperheroesId = given()
                .request("GET", getSuperheroes)
                .getBody().jsonPath().getList("id");
    }

    @Test
    public void getSuperheroesList()
    {
        when()
                .request("GET", getSuperheroes)
                .then()
                .statusCode(200)
                .assertThat().body(matchesJsonSchemaInClasspath("superheroes_list_schema.json"));
    }

    //Bug, see more at https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.77w3mjer1sgg
    @Test
    public void createSuperhero()
    {
        given()
                .contentType(ContentType.JSON)
                .body(superhero.getPlayerJson())
                .request("POST", createSuperhero)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("fullName",equalTo(superhero.getFullName()))
                .body("birthDate",equalTo(superhero.getBirthDate().toString()))
                .body("city",equalTo(superhero.getCity()))
                .body("mainSkill",equalTo(superhero.getMainSkill()))
                .body("gender",equalTo(superhero.getGender().genderString))
                .body("phone",equalTo(superhero.getPhone()))
                .assertThat().body(matchesJsonSchemaInClasspath("superhero_schema.json"));
    }

    //Bug, see more at https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.2gabilnms3rm and https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.4b4bvp12fzxi
    @Test
    public void getSuperhero()
    {
        List<Integer> listSuperheroesId = given()
                .request("GET", getSuperheroes)
                .getBody().jsonPath().getList("id");
        for(int id : listSuperheroesId)
        {
            when()
                    .request("GET", String.format(getSuperhero,id))
                    .then()
                    .statusCode(200)
                    .assertThat().body(matchesJsonSchemaInClasspath("superhero_schema.json"));
        }
    }

    //Bug sometimes, see more https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.sklowgorxo1i
    @Test
    public void updateSuperhero()
    {
        Integer randomId = CollectionUtil.getRandomElement(listSuperheroesId);
        given()
                .contentType(ContentType.JSON)
                .body(superhero.getPlayerJson())
                .request("PUT", String.format(updateSuperhero,randomId))
                .then()
                .statusCode(200);
    }

    //Bug sometimes, see more at https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.5chid7hl8thw
    @Test
    public void deleteSuperhero()
    {
        Integer randomId = CollectionUtil.getRandomElement(listSuperheroesId);
        given()
                .contentType(ContentType.JSON)
                .request("DELETE", String.format(deleteSuperhero,randomId))
                .then()
                .statusCode(200);
    }

    //Bug, https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.2gabilnms3rm and https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.4b4bvp12fzxi and https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.sklowgorxo1i and https://docs.google.com/document/d/1lcAUxZqi34d1W-xSSDs8EFFSi8m3ActtNRhfO_4hfcE/edit#heading=h.5chid7hl8thw
    @Test
    public void fullSuperheroCycleTest()
    {
        Superhero newSuperhero = new Superhero().RandomSuperhero();
        String superheroId = given()
                .contentType(ContentType.JSON)
                .body(newSuperhero.getPlayerJson())
                .request("POST", createSuperhero)
                .getBody().jsonPath().getString("id");
        assertThat(listSuperheroesId,hasItem(Integer.parseInt(superheroId)));
        when()
                .request("GET", String.format(getSuperhero,superheroId))
                .then()
                .statusCode(200)
                .body("id", equalTo(Integer.parseInt(superheroId)))
                .body("fullName",equalTo(newSuperhero.getFullName()))
                .body("birthDate",equalTo(newSuperhero.getBirthDate().toString()))
                .body("city",equalTo(newSuperhero.getCity()))
                .body("mainSkill",equalTo(newSuperhero.getMainSkill()))
                .body("gender",equalTo(newSuperhero.getGender().genderString))
                .body("phone",equalTo(newSuperhero.getPhone()))
                .assertThat().body(matchesJsonSchemaInClasspath("superhero_schema.json"));
        Superhero newSuperheroForUpdate = new Superhero().RandomSuperhero();
        given()
                .contentType(ContentType.JSON)
                .body(newSuperheroForUpdate.getPlayerJson())
                .request("PUT", String.format(updateSuperhero,superheroId))
                .then()
                .statusCode(200);
        when()
                .request("GET", String.format(getSuperhero,superheroId))
                .then()
                .statusCode(200)
                .body("id", equalTo(Integer.parseInt(superheroId)))
                .body("fullName",equalTo(newSuperheroForUpdate.getFullName()))
                .body("birthDate",equalTo(newSuperheroForUpdate.getBirthDate().toString()))
                .body("city",equalTo(newSuperheroForUpdate.getCity()))
                .body("mainSkill",equalTo(newSuperheroForUpdate.getMainSkill()))
                .body("gender",equalTo(newSuperheroForUpdate.getGender().genderString))
                .body("phone",equalTo(newSuperheroForUpdate.getPhone()))
                .assertThat().body(matchesJsonSchemaInClasspath("superhero_schema.json"));
        given()
                .contentType(ContentType.JSON)
                .request("DELETE", String.format(deleteSuperhero,superheroId))
                .then()
                .statusCode(200);
        when()
                .request("GET", String.format(getSuperhero,superheroId))
                .then()
                .statusCode(400)
                .body("code", equalTo("NOT_FOUND"));
    }
}
