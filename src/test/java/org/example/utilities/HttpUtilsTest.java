package org.example.utilities;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HttpUtilsTest {

    @Test
    void fromJSON() {
        final var ex1 = """
                {
                  "goodName"  : "good1",
                  "groupName" : "group1",
                  "quantity"  : "10",
                  "price"     : "11.23"
                }
                """;
        final var ex2 = """
                {
                  "goodName"  : "good2"
                }
                """;
        final var ex3 = """
                {
                  "goodName"  : "good3",
                  "unnecessaryField"  : "12",
                  "quantity" : "32"
                }
                """;
        try {
            var params1 = HttpUtils.fromJSON(ex1.getBytes());
            assertEquals(params1.getGoodName(), "good1");
            assertEquals(params1.getGroupName(), "group1");
            assertEquals(params1.getQuantity(), 10);
            assertEquals(params1.getPrice(), 11.23);

            var params2 = HttpUtils.fromJSON(ex2.getBytes());
            assertEquals(params2.getGoodName(), "good2");
            assertEquals(params2.getGroupName(), "");
            assertEquals(params2.getQuantity(), 0);
            assertEquals(params2.getPrice(), 0.0);

            var params3 = HttpUtils.fromJSON(ex3.getBytes());
            assertEquals(params3.getGoodName(), "good3");
            assertEquals(params3.getGroupName(), "");
            assertEquals(params3.getQuantity(), 32);
            assertEquals(params3.getPrice(), 0.0);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void extractQueryParam() {
        final var ex1 = "id=1&name=good1";
        final var id = HttpUtils.extractQueryParam(ex1, "id");
        final var name = HttpUtils.extractQueryParam(ex1, "name");
        assertTrue(id.isPresent());
        assertTrue(name.isPresent());
        assertEquals(id.get(), "1");
        assertEquals(name.get(), "good1");
    }

    @Test
    void extractQueryParamsFromQuery() {
        {
            final var uri = "groupName=group1&goodName=good1&quantity=123&price=12.32";
            final var params = HttpUtils.extractParamsFromQuery(uri);
            assertTrue(params.isPresent());
            assertEquals(params.get().getGoodName(), "good1");
            assertEquals(params.get().getGroupName(), "group1");
            assertEquals(params.get().getQuantity(), 123);
            assertEquals(params.get().getPrice(), 12.32);
        }
        {
            final var uri = "goodName=good1&price=12.32&quantity=123&groupName=group1";
            final var params = HttpUtils.extractParamsFromQuery(uri);
            assertTrue(params.isPresent());
            assertEquals(params.get().getGoodName(), "good1");
            assertEquals(params.get().getGroupName(), "group1");
            assertEquals(params.get().getQuantity(), 123);
            assertEquals(params.get().getPrice(), 12.32);
        }
        {
            final var uri = "goodName=good1&price=12.32";
            final var params = HttpUtils.extractParamsFromQuery(uri);
            assertTrue(params.isPresent());
            assertEquals(params.get().getGoodName(), "good1");
            assertEquals(params.get().getGroupName(), "");
            assertEquals(params.get().getQuantity(), 0);
            assertEquals(params.get().getPrice(), 12.32);
        }
        {
            final var uri = "something=som321&goodName=good1&otherNeedless=needless&price=12.32&unneededField=123";
            final var params = HttpUtils.extractParamsFromQuery(uri);
            assertTrue(params.isPresent());
            assertEquals(params.get().getGoodName(), "good1");
            assertEquals(params.get().getGroupName(), "");
            assertEquals(params.get().getQuantity(), 0);
            assertEquals(params.get().getPrice(), 12.32);
        }
    }
}