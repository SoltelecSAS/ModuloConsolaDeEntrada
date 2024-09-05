package com.soltelec.consolaentrada.utilities;

public class CifraDesifra {
    private static char patron = 's';

   public CifraDesifra() {
   }

   public static String cifra(String cadena, char patron) {
      char[] secStr = cadena.toCharArray();
      String strEncript = "";

      for(int n = 0; n < secStr.length; ++n) {
         char c = (char)(secStr[n] ^ patron);
         strEncript = strEncript + c;
      }

      return strEncript;
   }

   public static String deCifrar(String cadena) {
      char[] secStr = cadena.toCharArray();
      String strEncript = "";

      for(int n = 0; n < secStr.length; ++n) {
         char c = (char)(secStr[n] ^ patron);
         strEncript = strEncript + c;
      }

      return strEncript;
   }

   public static void servicio() {
   }
}
