import com.syos.factory.MySQLDAOFactory;
import com.syos.state.AppContext;
import com.syos.state.LoginState;
import java.util.Scanner;

/**
 * Application entry point.
 * 
 * Design Patterns used at startup:
 * - Factory Pattern: MySQLDAOFactory creates all DAO instances
 * - State Pattern: AppContext manages state transitions (Login → Menu → ...)
 * - Singleton Pattern: Factory and DatabaseConnection are singletons
 * - Observer Pattern: Stock observers are registered via the factory
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Factory Pattern — Get the singleton factory (also sets up observers)
        MySQLDAOFactory factory = MySQLDAOFactory.getInstance();

        // State Pattern — Start with LoginState, AppContext manages transitions
        AppContext context = new AppContext(new LoginState());
        context.run(scanner, factory);
    }
}