package Binarios;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    private RandomAccessFile rcods, remps;

    public EmpleadoManager() {
        try {
            File mf = new File("company");
            mf.mkdir();

            rcods = new RandomAccessFile("company/codigo.emp", "rw");
            remps = new RandomAccessFile("company/empleado.emp", "rw");

            initCodes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void initCodes() throws IOException {
        if (rcods.length() == 0) {
            rcods.writeInt(1);
        }
    }

    private int getCode() throws IOException {
        rcods.seek(0);
        int code = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(code + 1);
        return code;
    }

    public void addEmployee(String name, double salary) throws IOException {
        remps.seek(remps.length());

        int code = getCode();

        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(salary);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);

        createEmployeeFolder(code);

        System.out.println("Empleado agregado con codigo: " + code);
    }

    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }

    private RandomAccessFile salesFilefor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        String path = dirPadre + "/ventas" + yearActual + ".emp";

        return new RandomAccessFile(path, "rw");
    }

    private void createSaleFileFor(int code) throws IOException {
        RandomAccessFile ryear = salesFilefor(code);

        if (ryear.length() == 0) {
            for (int mes = 0; mes < 12; mes++) {
                ryear.writeDouble(0);
                ryear.writeBoolean(false);
            }
        }

        ryear.close();
    }

    private void createEmployeeFolder(int code) throws IOException {
        File edir = new File(employeeFolder(code));
        edir.mkdir();

        createSaleFileFor(code);
    }

    public void employeeList() throws IOException {
        remps.seek(0);

        while (remps.getFilePointer() < remps.length()) {
            int code = remps.readInt();
            String name = remps.readUTF();
            double sal = remps.readDouble();
            Date fecha = new Date(remps.readLong());

            if (remps.readLong() == 0) {
                System.out.println(code + "-" + name + " - Lps. " + sal
                        + " Contratado el: " + fecha);
            }
        }
    }

    private boolean isEmployeeActive(int code) throws IOException {
        remps.seek(0);

        while (remps.getFilePointer() < remps.length()) {
            int code1 = remps.readInt();
            long pos = remps.getFilePointer();

            remps.readUTF();
            remps.skipBytes(16);

            if (remps.readLong() == 0 && code1 == code) {
                remps.seek(pos);
                return true;
            }
        }

        return false;
    }

    public boolean fireEmployee(int code) throws IOException {
        if (isEmployeeActive(code)) {
            String name = remps.readUTF();

            remps.skipBytes(16);
            remps.writeLong(new Date().getTime());

            System.out.println("Despidiendo a " + name);

            return true;
        }

        return false;
    }

    public void addSaleToEmployee(int code, double monto) throws IOException {
        if (!isEmployeeActive(code)) {
            System.out.println("Empleado no encontrado");
            return;
        }

        createSaleFileFor(code);

        int mesActual = Calendar.getInstance().get(Calendar.MONTH);
        long posicion = mesActual * 9L;

        RandomAccessFile ryear = salesFilefor(code);

        ryear.seek(posicion);
        double ventas = ryear.readDouble();

        ryear.seek(posicion);
        ryear.writeDouble(ventas + monto);

        ryear.close();

        System.out.println("Venta agregada");
    }

    public RandomAccessFile billsFilefor(int code) throws IOException {
        String path = employeeFolder(code) + "/recibos.emp";

        return new RandomAccessFile(path, "rw");
    }

    public boolean isEmployeePayed(int code) throws IOException {
        createSaleFileFor(code);

        int mesActual = Calendar.getInstance().get(Calendar.MONTH);
        long posicion = mesActual * 9L;

        RandomAccessFile ryear = salesFilefor(code);

        ryear.seek(posicion + 8);
        boolean pagado = ryear.readBoolean();

        ryear.close();

        return pagado;
    }

    public void payEmployee(int code) throws IOException {
        if (!isEmployeeActive(code) || isEmployeePayed(code)) {
            System.out.println("No se pudo pagar");
            return;
        }

        String name = remps.readUTF();
        double salarioBase = remps.readDouble();

        Calendar fecha = Calendar.getInstance();

        int yearActual = fecha.get(Calendar.YEAR);
        int mesActual = fecha.get(Calendar.MONTH);
        long posicion = mesActual * 9L;

        RandomAccessFile ryear = salesFilefor(code);

        ryear.seek(posicion);
        double ventas = ryear.readDouble();

        double sueldo = salarioBase + (ventas * 0.10);
        double deduccion = sueldo * 0.035;
        double total = sueldo - deduccion;

        RandomAccessFile rbills = billsFilefor(code);

        rbills.seek(rbills.length());
        rbills.writeLong(fecha.getTimeInMillis());
        rbills.writeDouble(sueldo);
        rbills.writeDouble(deduccion);
        rbills.writeInt(yearActual);
        rbills.writeInt(mesActual + 1);

        rbills.close();

        ryear.seek(posicion + 8);
        ryear.writeBoolean(true);

        ryear.close();

        System.out.printf("Empleado %s se le pago Lps. %.2f%n", name, total);
    }

    private boolean findEmployee(int code) throws IOException {
        remps.seek(0);

        while (remps.getFilePointer() < remps.length()) {
            int code1 = remps.readInt();
            long pos = remps.getFilePointer();

            remps.readUTF();
            remps.skipBytes(24);

            if (code1 == code) {
                remps.seek(pos);
                return true;
            }
        }

        return false;
    }

    public void printEmployee(int code) throws IOException {
        if (!findEmployee(code)) {
            System.out.println("Empleado no encontrado");
            return;
        }

        String name = remps.readUTF();
        double salary = remps.readDouble();
        Date fechaContratacion = new Date(remps.readLong());

        remps.readLong();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Codigo: " + code);
        System.out.println("Nombre: " + name);
        System.out.println("Salario: " + salary);
        System.out.println("Fecha de contratacion: "
                + formato.format(fechaContratacion));

        createSaleFileFor(code);

        RandomAccessFile ryear = salesFilefor(code);
        double totalVentas = 0;

        for (int mes = 1; mes <= 12; mes++) {
            double ventas = ryear.readDouble();

            ryear.readBoolean();

            totalVentas += ventas;

            System.out.println("Mes " + mes + " : " + ventas);
        }

        ryear.close();

        System.out.println("Total de ventas del anio: " + totalVentas);

        RandomAccessFile rbills = billsFilefor(code);

        long totalPagos = rbills.length() / 32;

        rbills.close();

        System.out.println("Total de pagos realizados: " + totalPagos);
    }
}