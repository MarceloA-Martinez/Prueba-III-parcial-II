public class Main {
    public static void main(String[] args) {
        Scanner n = new Scanner(System.in);

        try {
            EmployeeManager manager = new EmployeeManager();

            int option;

            do {
                System.out.println("\nMENU");
                System.out.println("1. Agregar venta");
                System.out.println("2. Pagar empleado");
                System.out.println("3. Mostrar reporte de empleado");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opcion: ");

                option = n.nextInt();

                switch (option) {
                    case 1:
                        System.out.print("Codigo del empleado: ");
                        int saleCode = n.nextInt();

                        System.out.print("Monto de la venta: ");
                        double amount = n.nextDouble();

                        manager.addSaleToEmployee(saleCode, amount);
                        break;

                    case 2:
                        System.out.print("Codigo del empleado: ");
                        int payCode = n.nextInt();

                        manager.payEmployee(payCode);
                        break;

                    case 3:
                        System.out.print("Codigo del empleado: ");
                        int reportCode = n.nextInt();

                        manager.printEmployee(reportCode);
                        break;

                    case 4:
                        System.out.println("Programa finalizado");
                        break;

                    default:
                        System.out.println("Opcion invalida");
                }

            } while (option != 4);

        } catch (Exception e) {
            System.out.println("Ocurrio un error: " + e.getMessage());
        }
    }
}
