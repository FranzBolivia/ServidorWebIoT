package com.example.servidorwebiot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.I2cDevice;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity implements WebServer.WebserverListener {
    private WebServer server;
    private final String PIN_LED = "BCM18";
    public Gpio mLedGpio;
    private PeripheralManager service;

    private static final String IN_I2C_NOMBRE = "I2C1"; // Puerto de entrada
    private static final int IN_I2C_DIRECCION = 0x48; // Dirección de entrada
    I2cDevice i2c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server = new WebServer(8180, this, this);
        service = PeripheralManager.getInstance();



        try {
            mLedGpio = service.openGpio(PIN_LED);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        } catch (IOException e) {
            Log.e(TAG, "Error en el API PeripheralIO", e);
        }

/*

Para habilitar el FotoResistor , debe conectarse el L2C
        try {
            Log.i("Entro", "Aqui");
            i2c = service.openI2cDevice(IN_I2C_NOMBRE, IN_I2C_DIRECCION);
            byte[] config = new byte[2];
            config[0] = (byte) 0x41; // byte de control: 0100 00 01
            config[1] = (byte) 0xff; // valor de salida (128/255)
            i2c.write(config, config.length); // escribimos 2 bytes
            byte[] buffer = new byte[4];
            i2c.read(buffer, buffer.length); // leemos 4 bytes
            String s = "";
            Log.i("Entro", String.valueOf(buffer.length));

            String temporal= String.valueOf(buffer[2] & 0xFF);
            Log.i("Valor del Fotoresistor",temporal);

            for (int i = 0; i < buffer.length; i++) {
                s += " byte " + i + ": " + (buffer[i] & 0xFF);
            }
            Log.i(TAG, s); // mostramos salida

            i2c.close(); // cerramos i2c
            i2c = null; // liberamos memria

        } catch (IOException e) {
            Log.e(TAG, "Error en al acceder a dispositivo I2C", e);
        }
*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.stop();
        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error en el API PeripheralIO", e);
            } finally {
                mLedGpio = null;
            }
        }
    }

    @Override
    public String switchLEDon() {
        try {
            mLedGpio.setValue(true);


                Log.i("Entro", "Aqui");
                i2c = service.openI2cDevice(IN_I2C_NOMBRE, IN_I2C_DIRECCION);
                byte[] config = new byte[2];
                config[0] = (byte) 0x41; // byte de control: 0100 00 01
                config[1] = (byte) 0xFF; // valor de salida (128/255)
                i2c.write(config, config.length); // escribimos 2 bytes
                byte[] buffer = new byte[4];
                i2c.read(buffer, buffer.length); // leemos 4 bytes
                String s = "";
                Log.i("Entro", String.valueOf(buffer.length));

                String temporal= String.valueOf(buffer[2] & 0xFF);
                Log.i("Valor del Fotoresistor",temporal);

                for (int i = 0; i < buffer.length; i++) {
                    s += " byte " + i + ": " + (buffer[i] & 0xFF);
                }
                Log.i(TAG, s); // mostramos salida

                i2c.close(); // cerramos i2c
                i2c = null; // liberamos memria

            Log.i(TAG, "LED switched ON");
            return temporal;
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
            return "error";
        }


    }

    @Override
    public void switchLEDoff() {
        try {
            mLedGpio.setValue(false);
            Log.i(TAG, "LED switched OFF");
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    public Boolean getLedStatus() {
      try {
            return mLedGpio.getValue();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
            return false;
        }

    }
}