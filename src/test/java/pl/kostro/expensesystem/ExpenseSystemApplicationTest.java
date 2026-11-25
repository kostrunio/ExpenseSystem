package pl.kostro.expensesystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.web.server.LocalServerPort;
import com.vaadin.flow.router.Route;
import pl.kostro.expensesystem.model.service.ExpenseService;
import pl.kostro.expensesystem.model.service.ExpenseSheetService;
import pl.kostro.expensesystem.model.service.RealUserService;
import pl.kostro.expensesystem.ui.ExpenseSystemView;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:expensesystem;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "vaadin.launch-browser=false"
})
class ExpenseSystemApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @LocalServerPort
    private int port;

    @Test
    void applicationLoadsMigratedScreensAndBusinessServices() throws Exception {
        assertThat(ExpenseSystemView.class).hasAnnotation(Route.class);
        assertThat(applicationContext.getBean(RealUserService.class)).isNotNull();
        assertThat(applicationContext.getBean(ExpenseSheetService.class)).isNotNull();
        assertThat(applicationContext.getBean(ExpenseService.class)).isNotNull();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/")).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).containsIgnoringCase("vaadin");
    }
}
