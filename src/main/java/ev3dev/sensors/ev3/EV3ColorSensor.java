package ev3dev.sensors.ev3;

import ev3dev.sensors.BaseSensor;
import ev3dev.sensors.GenericMode;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
import lejos.robotics.ColorIdentifier;
import lejos.robotics.LampController;


/**
 * <b>EV3 color sensors</b><br>
 * The digital EV3 Color Sensor distinguishes between eight different colors. It also serves as a light sensors by detecting light intensities.
 *
 * <p>
 * <b>Sensor configuration</b><br>
 * The flood light of the sensors can be put on or off using the setFloodlight methods.
 * 
 * <p>
 * 
 * See <a href="http://www.ev-3.net/en/archives/847"> Sensor Product page </a>
 * See <a href="http://sourceforge.net/p/lejos/wiki/Sensor%20Framework/"> The
 *      leJOS sensors framework</a>
 * See {@link lejos.robotics.SampleProvider leJOS conventions for
 *      SampleProviders}
 * 
 *      <p>
 * 
 * 
 * @author Andy Shaw
 * @author Juan Antonio Breña Moral
 * 
 */
public class EV3ColorSensor extends BaseSensor implements LampController, ColorIdentifier {
    // TODO: decide what to do to the LampController and ColorIdentifier interfaces

    private static final String LEGO_EV3_COLOR_SENSOR = "lego-ev3-color";

    private static final String COL_COLOR = "COL-COLOR"; // mode 0; color ID
    private static final String COL_REFLECT = "COL-REFLECT";// mode 1; reflected intensity
    private static final String COL_AMBIENT = "COL-AMBIENT";// mode 2; scaled ambient intensity
    private static final String COL_RGBRAW = "RGB-RAW"; // mode 3; raw RGB reflectivity
    private static final String COL_REFRAW = "REF-RAW"; // not used here; raw red reflectivity / ambient
    private static final String COL_CAL = "COL-CAL"; // not used here; maybe used for sensor bootstrap in LEGO

	public EV3ColorSensor(final Port portName) {
        super(portName, LEGO_UART_SENSOR, LEGO_EV3_COLOR_SENSOR);

        setModes(new SensorMode[]{
                new GenericMode(this, COL_COLOR, 1, "ColorID"),
                new GenericMode(this, COL_REFLECT, 1, "Red"),
                new GenericMode(this, COL_AMBIENT, 1, "Ambient"),
                new GenericMode(this, COL_RGBRAW, 3, "RGB")
        });
	}

    /** {@inheritDoc}
     */    
    @Override
    public int getColorID() {
        float[] sample = new float[1];
        getColorIDMode().fetchSample(sample, 0);
        return (int) sample[0];
    }

    /** {@inheritDoc}
     */
    @Override
    public boolean isFloodlightOn() {
        return getFloodlight() != Color.NONE;
    }

    /** {@inheritDoc}
     */
    @Override
    public int getFloodlight() {
        switch (this.getSystemMode()) {
            case COL_COLOR:
            case COL_RGBRAW:
                return Color.WHITE;
            case COL_REFRAW:
            case COL_REFLECT:
                return Color.RED;
            case COL_AMBIENT:
            case COL_CAL:
            default:
                return Color.NONE;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFloodlight(boolean floodlight) {
        setFloodlight(floodlight ? Color.RED : Color.BLUE);
    }

    /** {@inheritDoc}
     */    
    @Override
    public boolean setFloodlight(int color) {
        float[] dummy = new float[3];

        SensorMode m;
        switch (color) {
            case Color.NONE:
            case Color.BLUE:
                m = getAmbientMode();
                break;
            case Color.WHITE:
                m = getColorIDMode();
                break;
            case Color.RED:
                m = getRedMode();
                break;
            default:
                // TODO: Should we ignore a wrong color or throw an exception?
                throw new IllegalArgumentException("Invalid color specified");
        }
        m.fetchSample(dummy, 0);
        return true;
    }

    /**
     * <b>EV3 color sensors, Color ID mode</b><br>
     * Measures the color ID of a surface. The sensors can identify 8 unique colors (NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN).
     * 
     * <p>
     * <b>Size and content of the sample</b><br>
     * The sample contains one element containing the ID (0-7) of the detected color.
     * 
     * <p>
     * 
     * @return A sampleProvider
     * See {@link lejos.robotics.SampleProvider leJOS conventions for
     *      SampleProviders}
     */
    public SensorMode getColorIDMode() {
        return getMode(0);
    }

    /**
     * <b>EV3 color sensors, Red mode</b><br>
     * Measures the level of reflected light from the sensors RED LED. 
     * 
     * <p>
     * <b>Size and content of the sample</b><br>
     * The sample contains one element containing the intensity level (Normalized between 0 and 1) of reflected light.
     * 
     * <p>
     * 
     * @return A sampleProvider
     * See {@link lejos.robotics.SampleProvider leJOS conventions for
     *      SampleProviders}
     */
    public SensorMode getRedMode() {
        return getMode(1);
    }

    /**
     * <b>EV3 color sensors, Ambient mode</b><br>
     * Measures the level of ambient light while the sensors lights are off. 
     * 
     * <p>
     * <b>Size and content of the sample</b><br>
     * The sample contains one element containing the intensity level (Normalized between 0 and 1) of ambient light.
     * 
     * <p>
     * 
     * @return A sampleProvider
     * See {@link lejos.robotics.SampleProvider leJOS conventions for
     *      SampleProviders}
     */
    public SensorMode getAmbientMode() {
        return getMode(2);
    }

    /**
     * get a sample provider that returns the light values (RGB) when illuminated by a
     * white light source.
     * @return the sample provider
     */
    /**
     * <b>EV3 color sensors, RGB mode</b><br>
     * Measures the level of red, green and blue light when illuminated by a white light source.. 
     * 
     * <p>
     * <b>Size and content of the sample</b><br>
     * The sample contains 3 elements containing the intensity level (Normalized between 0 and 1) of red, green and blue light respectivily.
     * 
     * <p>
     * 
     * @return A sampleProvider
     * See {@link lejos.robotics.SampleProvider leJOS conventions for
     *      SampleProviders}
     */
    public SensorMode getRGBMode() {
        return getMode(3);
    }

}
