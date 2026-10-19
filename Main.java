import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TecniHogarManager manager = new TecniHogarManager();
        Scanner scanner = new Scanner(System.in);
        int option = 0;

        System.out.println("==================================================");
        System.out.println("   SISTEMA DE GESTIÓN TÉCNICA - TECNIHOGAR (UVG)  ");
        System.out.println("==================================================");

        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Cotizar un servicio (sin registrar)");
            System.out.println("2. Registrar una nueva orden");
            System.out.println("3. Registrar una nueva cuadrilla y técnicos");
            System.out.println("4. Gestionar jornadas de trabajo (Crear, Asignar, Iniciar, Resultados, Cerrar)");
            System.out.println("5. Consultar órdenes y cuadrillas");
            System.out.println("6. Generar reportes gerenciales (Ingresos y Estados)");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                String input = scanner.nextLine();
                option = Integer.parseInt(input);

                switch (option) {
                    case 1:
                        handleQuote(manager, scanner);
                        break;
                    case 2:
                        handleRegisterOrder(manager, scanner);
                        break;
                    case 3:
                        handleRegisterGroup(manager, scanner);
                        break;
                    case 4:
                        handleWorkingTimesMenu(manager, scanner);
                        break;
                    case 5:
                        handleConsultations(manager);
                        break;
                    case 6:
                        handleReports(manager);
                        break;
                    case 7:
                        System.out.println("Saliendo del sistema. ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Error de operación: " + e.getMessage());
            }

        } while (option != 7);

        scanner.close();
    }

    private static void handleQuote(TecniHogarManager manager, Scanner scanner) {
        try {
            Order tempOrder = createOrderFromInput(scanner, "COTIZACIÓN");
            if (tempOrder != null) {
                System.out.printf("\n--- RESULTADO DE COTIZACIÓN ---\n");
                System.out.printf("Categoría: %s\n", tempOrder.categoryName());
                System.out.printf("Costo Estimado: Q%.2f\n", tempOrder.quote());
                System.out.printf("Tiempo Estimado: %d días hábiles\n", tempOrder.estimatedDays());
                System.out.println("(Nota: Esta operación es una cotización y no ha alterado ningún registro ni ingreso).");
            }
        } catch (Exception e) {
            System.out.println("No se pudo generar la cotización: " + e.getMessage());
        }
    }

    private static void handleRegisterOrder(TecniHogarManager manager, Scanner scanner) {
        try {
            Order newOrder = createOrderFromInput(scanner, "REGISTRO");
            if (newOrder != null) {
                manager.registerOrder(newOrder);
                System.out.printf("¡Orden [%s] registrada exitosamente! Se cobró el monto completo de Q%.2f y quedó en estado PENDING.\n",
                        newOrder.getId(), newOrder.quote());
            }
        } catch (Exception e) {
            System.out.println("Error al registrar la orden: " + e.getMessage());
        }
    }

    private static Order createOrderFromInput(Scanner scanner, String mode) {
        System.out.println("\nSeleccione la Categoría de Servicio:");
        System.out.println("1. Diagnóstico");
        System.out.println("2. Instalación");
        System.out.println("3. Instalación Eléctrica Certificada");
        System.out.println("4. Mantenimiento de Aire Acondicionado");
        System.out.print("Opción: ");
        int catOpt = Integer.parseInt(scanner.nextLine());

        System.out.print("Ingrese código único de la orden: ");
        String id = scanner.nextLine();
        System.out.print("Nombre del cliente: ");
        String client = scanner.nextLine();
        System.out.print("Dirección: ");
        String address = scanner.nextLine();
        System.out.print("Horas estimadas de trabajo (decimales permitidos): ");
        float hours = Float.parseFloat(scanner.nextLine());

        System.out.println("Zona del servicio:");
        System.out.println("1. Metropolitana");
        System.out.println("2. Periférica");
        System.out.print("Opción de Zona: ");
        int zoneOpt = Integer.parseInt(scanner.nextLine());
        WorkZone zone = (zoneOpt == 1) ? WorkZone.Metropolitan : WorkZone.Peripheral;

        switch (catOpt) {
            case 1:
                return new Diagnosis(id, client, address, hours, zone);
            case 2:
                System.out.print("Cantidad de puntos a instalar (entero positivo): ");
                int points = Integer.parseInt(scanner.nextLine());
                return new Installation(id, client, address, hours, zone, points);
            case 3:
                System.out.print("Cantidad de puntos a instalar (entero positivo): ");
                int cPoints = Integer.parseInt(scanner.nextLine());
                System.out.print("Costo declarado de materiales (Q): ");
                float matCost = Float.parseFloat(scanner.nextLine());
                return new CertifiedInstallation(id, client, address, hours, zone, cPoints, matCost);
            case 4:
                System.out.print("Capacidad del equipo en BTU (9000 - 60000): ");
                int btu = Integer.parseInt(scanner.nextLine());
                return new ACMaintenance(id, client, address, hours, zone, btu);
            default:
                throw new IllegalArgumentException("Categoría de servicio inválida.");
        }
    }

    private static void handleRegisterGroup(TecniHogarManager manager, Scanner scanner) {
        System.out.print("Código de la cuadrilla: ");
        String id = scanner.nextLine();
        System.out.print("Capacidad máxima de horas por jornada: ");
        float capacity = Float.parseFloat(scanner.nextLine());
        System.out.print("¿Está certificada en refrigeración? (s/n): ");
        boolean cert = scanner.nextLine().trim().equalsIgnoreCase("s");

        Group group = new Group(id, capacity, cert);

        System.out.print("¿Cuántos técnicos desea agregar a esta cuadrilla? ");
        int techCount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < techCount; i++) {
            System.out.printf("  Técnico %d - ID: ", i + 1);
            String tId = scanner.nextLine();
            System.out.printf("  Técnico %d - Nombre: ", i + 1);
            String tName = scanner.nextLine();
            group.addTechnician(new Technician(tId, tName));
        }

        manager.registerGroup(group);
        System.out.println("¡Cuadrilla registrada con éxito!");
    }

    private static void handleWorkingTimesMenu(TecniHogarManager manager, Scanner scanner) {
        System.out.println("\n--- GESTIÓN DE JORNADAS ---");
        System.out.println("1. Crear nueva jornada (En preparación)");
        System.out.println("2. Asignar orden pendiente a jornada");
        System.out.println("3. Retirar orden de jornada");
        System.out.println("4. Iniciar jornada");
        System.out.println("5. Registrar resultados de visitas (Completada / Fallida)");
        System.out.println("6. Cerrar jornada");
        System.out.print("Seleccione opción: ");
        int op = Integer.parseInt(scanner.nextLine());

        switch (op) {
            case 1:
                System.out.print("ID de la nueva jornada: ");
                String wtId = scanner.nextLine();
                System.out.print("ID de la cuadrilla asignada: ");
                String gId = scanner.nextLine();
                Group g = manager.findGroupById(gId);
                if (g == null) {
                    System.out.println("Error: Cuadrilla no encontrada.");
                    return;
                }
                WorkingTime wt = new WorkingTime(wtId, g);
                manager.registerWorkingTime(wt);
                System.out.println("¡Jornada creada en estado IN_PREPARATION!");
                break;
            case 2:
                System.out.print("ID de la jornada: ");
                WorkingTime wTarget = manager.findWorkingTimeById(scanner.nextLine());
                if (wTarget == null) { System.out.println("Jornada no encontrada."); return; }
                System.out.print("ID de la orden pendiente a asignar: ");
                Order oTarget = manager.findOrderById(scanner.nextLine());
                if (oTarget == null) { System.out.println("Orden no encontrada."); return; }

                wTarget.assignOrder(oTarget, manager.getWorkingTimes());
                System.out.println("¡Orden asignada exitosamente a la jornada!");
                break;
            case 3:
                System.out.print("ID de la jornada: ");
                WorkingTime wRem = manager.findWorkingTimeById(scanner.nextLine());
                if (wRem == null) { System.out.println("Jornada no encontrada."); return; }
                System.out.print("ID de la orden a retirar: ");
                Order oRem = manager.findOrderById(scanner.nextLine());
                if (oRem == null) { System.out.println("Orden no encontrada."); return; }

                wRem.removeOrder(oRem);
                System.out.println("¡Orden retirada y devuelta a estado pendiente!");
                break;
            case 4:
                System.out.print("ID de la jornada a iniciar: ");
                WorkingTime wStart = manager.findWorkingTimeById(scanner.nextLine());
                if (wStart == null) { System.out.println("Jornada no encontrada."); return; }
                wStart.start(manager);
                System.out.println("¡Jornada iniciada! Las órdenes pasaron a IN_ATTENTION y se calcularon cargos por visitas adicionales.");
                break;
            case 5:
                System.out.print("ID de la jornada en curso: ");
                WorkingTime wRes = manager.findWorkingTimeById(scanner.nextLine());
                if (wRes == null) { System.out.println("Jornada no encontrada."); return; }
                System.out.println("Órdenes en esta jornada:");
                for (Order ord : wRes.getOrders()) {
                    if (ord.getStatus() == OrderStatus.In_attention) {
                        System.out.printf(" - Orden [%s] (%s). Resultado? (1: Completa, 2: Fallida): ", ord.getId(), ord.categoryName());
                        int res = Integer.parseInt(scanner.nextLine());
                        if (res == 1) {
                            ord.setStatus(OrderStatus.Complete);
                            System.out.println("   Marcada como COMPLETE.");
                        } else {
                            ord.incrementVisitsCount();
                            if (ord.getVisitsCount() >= ord.maxVisits()) {
                                ord.setStatus(OrderStatus.Cancelled);
                                System.out.println("   Límite máximo de visitas alcanzado. Orden CANCELLED.");
                            } else {
                                ord.setStatus(OrderStatus.Pending);
                                System.out.printf("   Visita fallida (Visitas: %d/%d). Regresa a PENDING.\n", ord.getVisitsCount(), ord.maxVisits());
                            }
                        }
                    }
                }
                break;
            case 6:
                System.out.print("ID de la jornada a cerrar: ");
                WorkingTime wClose = manager.findWorkingTimeById(scanner.nextLine());
                if (wClose == null) { System.out.println("Jornada no encontrada."); return; }
                wClose.close();
                System.out.println("¡Jornada cerrada con éxito y cuadrilla liberada!");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private static void handleConsultations(TecniHogarManager manager) {
        System.out.println("\n--- CONSULTA DE ÓRDENES ---");
        for (Order o : manager.getOrders()) {
            System.out.println(o);
        }

        System.out.println("\n--- CONSULTA DE CUADRILLAS ---");
        for (Group g : manager.getGroups()) {
            System.out.println(g);
        }
    }

    private static void handleReports(TecniHogarManager manager) {
        System.out.println("\n==================================================");
        System.out.println("               REPORTES GERENCIALES               ");
        System.out.println("==================================================");

        // Ingresos
        System.out.println("\n[1] Ingresos Financieros:");
        System.out.printf(" - Ingresos por Registro de Órdenes: Q%.2f\n", manager.getRegistrationIncome());
        System.out.printf(" - Ingresos por Visitas Adicionales: Q%.2f\n", manager.getAdditionalVisitsIncome());
        System.out.printf(" - INGRESOS TOTALES: Q%.2f\n", manager.getTotalIncome());

        System.out.println("\n - Ingresos por Categoría:");
        String[] cats = {"Diagnóstico", "Instalación", "Instalación Eléctrica Certificada", "Mantenimiento de Aire Acondicionado"};
        for (String c : cats) {
            System.out.printf("   * %s: Q%.2f\n", c, manager.getIncomeByCategory(c));
        }

        // Conteo de órdenes por categoría y estado
        System.out.println("\n[2] Estado y Conteo de Órdenes:");
        for (String c : cats) {
            long totalCat = manager.getOrders().stream().filter(o -> o.categoryName().equalsIgnoreCase(c)).count();
            System.out.printf(" > %s (Total: %d):\n", c, totalCat);
            for (OrderStatus status : OrderStatus.values()) {
                long count = manager.getOrders().stream()
                        .filter(o -> o.categoryName().equalsIgnoreCase(c) && o.getStatus() == status)
                        .count();
                if (count > 0) {
                    System.out.printf("   - %s: %d\n", status, count);
                }
            }
        }

        // Jornadas no cerradas
        System.out.println("\n[3] Jornadas Activas / No Cerradas:");
        boolean foundActive = false;
        for (WorkingTime wt : manager.getWorkingTimes()) {
            if (wt.getStatus() != WorkTimeStatus.Closed) {
                foundActive = true;
                float assignedHours = wt.calculateAssignedHours();
                float availableCapacity = wt.getGroup().getCapacityHoursPerDay() - assignedHours;
                System.out.printf(" - Jornada [%s] | Cuadrilla: %s | Estado: %s | Horas Asignadas: %.1f / %.1f | Capacidad Disponible: %.1f hrs\n",
                        wt.getId(), wt.getGroup().getId(), wt.getStatus(), assignedHours, wt.getGroup().getCapacityHoursPerDay(), availableCapacity);
            }
        }
        if (!foundActive) {
            System.out.println(" - No hay jornadas activas pendientes de cierre.");
        }
        System.out.println("==================================================");
    }
}