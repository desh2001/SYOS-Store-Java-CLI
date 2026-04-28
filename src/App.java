import com.syos.factory.MySQLDAOFactory;
import com.syos.state.AppContext;
import com.syos.state.RoleSelectionState;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MySQLDAOFactory factory = MySQLDAOFactory.getInstance();

        AppContext context = new AppContext(new RoleSelectionState());
        context.run(scanner, factory);
    }
}


