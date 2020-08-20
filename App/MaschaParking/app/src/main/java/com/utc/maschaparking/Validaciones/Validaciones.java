package com.utc.maschaparking.Validaciones;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.Locale;

public class Validaciones {

    //public  static  String URL = "https://brtechnology.000webhostapp.com/serviceAndroid/";
   public static String URL = "http://192.168.1.5/php/WebContent/mashcapark/serviceAndroid/";


    public static String typeUSer = "";
    public static String id = "";
    public static String cedula = "";
    public static String nombres = "";
    public static String apellidos = "";
    public static String direccion = "";
    public static String correo = "";
    public static String telefono = "";
    public static String idParq = "";


    public String getHost(String s, Context context2) {


        return URL;
    }

    public boolean isGPSProvider(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public boolean cedulaIsOk(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
// Coeficientes de validación cédula
// El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int verificador = Integer.parseInt(cedula.substring(9, 10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    } else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
            System.out.println("La Cédula ingresada es Incorrecta");
        }
        return cedulaCorrecta;
    }


    public boolean rucIsOk(String cedula) {
        int total = 0;
        int tamanoLongitudCedula = 13;
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int numeroProviancias = 24;
        int tercerdigito = 6;


        if (cedula.length() != tamanoLongitudCedula) {
            return false;
        } else {

            String ruc = cedula.substring(10, 13);


            if (cedula.matches("[0-9]*") && cedula.length() == tamanoLongitudCedula && ruc.equals("001")) {
                int provincia = Integer.parseInt(cedula.charAt(0) + "" + cedula.charAt(1));
                int digitoTres = Integer.parseInt(cedula.charAt(2) + "");
                if ((provincia > 0 && provincia <= numeroProviancias) && digitoTres < tercerdigito) {
                    int digitoVerificadorRecibido = Integer.parseInt(cedula.charAt(9) + "");
                    for (int i = 0; i < coeficientes.length; i++) {
                        int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(cedula.charAt(i) + "");
                        total = valor >= 10 ? total + (valor - 9) : total + valor;
                    }
                    int digitoVerificadorObtenido = total >= 10 ? (total % 10) != 0 ? 10 - (total % 10) : (total % 10) : total;
                    if (digitoVerificadorObtenido == digitoVerificadorRecibido) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }


    }


    public void goToNavigationDrive(Context context, double latitude, double longitude) {
        String uri = String.format(Locale.ENGLISH, "google.navigation:q=%1$f,%2$f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }


    public String appExists(String nombrePaquete, Context context) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
            int val = pm.getApplicationEnabledSetting(nombrePaquete);
            if (val == 0) {
                return "Enable";
            } else {

                return "Disable";
            }


        } catch (PackageManager.NameNotFoundException e) {
            return "NoExists";
        }
    }


    public static void openAppInfo(String packageName, Context context) {
        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }
    }

    public static void openPlayStore(String packageName, Context context) {

        try {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            context.startActivity(i);
        } catch (ActivityNotFoundException e) {

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            context.startActivity(i);
        }

    }

    public static boolean matriculaOk(String placa) {
        boolean ok = false;


        if (placa.length() == 7) {

            String city = placa.substring(0, 1);
            String abc = placa.substring(0, 3);
            String xxxx = placa.substring(3, 7);

            if (isString(abc) && isNumber(xxxx) && isCity(city)) {
                ok = true;
            }
        }

        return ok;

    }


    private static boolean isString(String string) {


        boolean x = true;
        for (int i = 0; i < 10; i++) {
            if (string.contains("" + i)) {
                x = false;
            }
        }
        return x;
    }

    private static boolean isNumber(String string) {

        if (string.toLowerCase().equals(string.toUpperCase())) {
            return true;
        } else {
            return false;
        }


    }

    private static boolean isCity(String string) {
        String validacion = "ABUCXHOEWGILRMVNSPQKTZYJ";


        if (validacion.contains(string.toUpperCase())) {

            return true;

        } else {
            return false;
        }
    }


}










