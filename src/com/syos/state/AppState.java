package com.syos.state;

import com.syos.factory.DAOFactory;
import java.util.Scanner;

public interface AppState {

    AppState handleState(Scanner scanner, DAOFactory factory);
}


