package ru.superheroes.service;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import ru.superheroes.service.models.Superhero;
import static io.restassured.RestAssured.when;



public class SuperheroTest {

    private static final String getSuperheroes = "/superheroes";
    private static final String createSuperhero = "/superheroes";
    private static final String getSuperhero = "/superheroes/%s";
    private static final String updateSuperhero = "/superheroes/%s";
    private static final String deleteSuperhero = "/superheroes/%s";
    private Superhero superhero = new Superhero().RandomSuperhero();

    @BeforeClass
    public void setUp() throws Exception {
        RestAssured.baseURI = "https://superhero.qa-test.csssr.com/";
        RestAssured.port = 443;
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
}
