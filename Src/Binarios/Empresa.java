package Binarios;

import java.io.IOException;
import java.util.Scanner;

public class Empresa {
    public static void main(String[] args) {
        Scanner lea = new Scanner(System.in);
        EmpleadoManager em = new EmpleadoManager();

        int opcion = 0;

        do {
            System.out.println("\n\nMENU\n");
            System.out.println("1- Agregar Empleado");
            System.out.println("2- Listar Empleados No Despedidos");
            System.out.println("3- Agregar Venta a Empleado");
            System.out.println("4- Pagar Empleado");
            System.out.println("5- Despedir Empleado");
            System.out.println("6- Imprimir Reporte de Empleado");
            System.out.println("7- Salir");
            System.out.print("Escoja una opcion: ");

            opcion = lea.nextInt();
            lea.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        System.out.print("Nombre del empleado: ");
                        String name = lea.nextLine();

                        System.out.print("Salario base: ");
                        double salary = lea.nextDouble();
                        lea.nextLine();

                        em.addEmployee(name, salary);
                        break;

                    case 2:
                        em.employeeList();
                        break;

                    case 3:
                        System.out.print("Codigo del empleado: ");
                        int codeVenta = lea.nextInt();

                        System.out.print("Monto de la venta: ");
                        double monto = lea.nextDouble();
                        lea.nextLine();

                        em.addSaleToEmployee(codeVenta, monto);
                        break;

                    case 4:
                        System.out.print("Codigo del empleado: ");
                        int codePago = lea.nextInt();
                        lea.nextLine();

                        em.payEmployee(codePago);
                        break;

                    case 5:
                        System.out.print("Codigo del empleado: ");
                        int codeDespedido = lea.nextInt();
                        lea.nextLine();

                        if (!em.fireEmployee(codeDespedido)) {
                            System.out.println("No se pudo despedir");
                        }

                        break;

                    case 6:
                        System.out.print("Codigo del empleado: ");
                        int codeReporte = lea.nextInt();
                        lea.nextLine();

                        em.printEmployee(codeReporte);
                        break;

                    case 7:
                        System.out.println("Programa finalizado");
                        break;

                    default:
                        System.out.println("Opcion incorrecta");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } while (opcion != 7);
    }
}