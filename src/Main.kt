/**
 * ===============================================================
 * Kotlin GUI Global Keypress Demo
 * ===============================================================
 *
 * This is a demo showing how a Kotlin / Swing GUI app can detect
 * and handle global keypress events (those not targeted at a
 * specific UI control).
 *
 * The trick is to always give the main window the 'focus'. This
 * means that any key press events are fed to the window, rather
 * than any other UI elements.
 *
 * To do do this, we stop other controls from being focussed by
 * setting 'isFocusable' to false when we add them, and in the
 * updateView(), we always 'requestFocus()' for the main window.
 */

import com.formdev.flatlaf.FlatDarkLaf
import java.awt.*
import java.awt.event.*
import javax.swing.*


/**
 * Launch the application
 */
fun main() {
    FlatDarkLaf.setup()     // Flat, dark look-and-feel
    val app = App()         // Create the app model
    MainWindow(app)         // Create and show the UI, using the app model
}


/**
 * The application class (model)
 * This is the place where any application data should be
 * stored, plus any application logic functions
 */
class App() {
    // Constants
    val MAX_VOL = 10
    val MIN_VOL = 0

    // Data fields
    var volume = MAX_VOL / 2

    // Application logic functions

    fun increaseVolume() {
        volume++
        if (volume > MAX_VOL) volume = MAX_VOL
    }

    fun decreaseVolume() {
        volume--
        if (volume < MIN_VOL) volume = MIN_VOL
    }
}


/**
 * Main UI window (view)
 * Defines the UI and responds to events
 * The app model should be passwd as an argument
 */
class MainWindow(val app: App) : JFrame(), ActionListener, KeyListener {

    // Fields to hold the UI elements
    private lateinit var infoLabel: JLabel
    private lateinit var upButton: JButton
    private lateinit var downButton: JButton

    /**
     * Configure the UI and display it
     */
    init {
        configureWindow()               // Configure the window
        addControls()                   // Build the UI

        setLocationRelativeTo(null)     // Centre the window
        isVisible = true                // Make it visible

        updateView()                    // Initialise the view with any app data
    }

    /**
     * Configure the main window
     */
    private fun configureWindow() {
        title = "Kotlin Swing GUI Key Demo"
        contentPane.preferredSize = Dimension(375, 250)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null

        pack()
    }

    /**
     * Populate the UI with UI controls
     */
    private fun addControls() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 24)
        val smallFont = Font(Font.SANS_SERIF, Font.PLAIN, 22)

        // Make this main window listen for key events
        this.addKeyListener(this)

        val messageLabel = JLabel("<html>Click the buttons or press the up and down arrows...")
        messageLabel.horizontalAlignment = SwingConstants.LEFT
        messageLabel.bounds = Rectangle(25, 25, 325, 50)
        messageLabel.font = smallFont
        add(messageLabel)

        infoLabel = JLabel("VOLUME INFO")
        infoLabel.horizontalAlignment = SwingConstants.LEFT
        infoLabel.bounds = Rectangle(25, 100, 325, 50)
        infoLabel.font = baseFont
        add(infoLabel)

        downButton = JButton("Down")
        downButton.bounds = Rectangle(25, 175, 150, 50)
        downButton.font = baseFont
        downButton.addActionListener(this)     // Handle any clicks
        downButton.isFocusable = false            // Prevent from capturing key events
        add(downButton)

        upButton = JButton("Up")
        upButton.bounds = Rectangle(200, 175, 150, 50)
        upButton.font = baseFont
        upButton.addActionListener(this)     // Handle any clicks
        upButton.isFocusable = false            // Prevent from capturing key events
        add(upButton)
    }


    /**
     * Update the UI controls based on the current state
     * of the application model
     */
    fun updateView() {
        infoLabel.text = "Volume: " + "▉".repeat(app.volume)

        this.requestFocus()     // Set focus to main window
    }

    /**
     * Handle any UI events (e.g. button clicks)
     * Usually this involves updating the application model
     * then refreshing the UI view
     */
    override fun actionPerformed(e: ActionEvent?) {
        // Update app data model based on button clicks
        when (e?.source) {
            upButton -> app.increaseVolume()
            downButton -> app.decreaseVolume()
        }

        // And ensure the UI view matched the updated app model data
        updateView()
    }

    /**
     * Key pressed fires when a key is pushed down
     * This is usually the place you'll want to have your code
     */
    override fun keyPressed(e: KeyEvent?) {
        println("Down: ${e?.keyCode}")

        // Check which key was pressed and act upon it
        when (e?.keyCode) {
            KeyEvent.VK_UP    -> app.increaseVolume()
            KeyEvent.VK_DOWN  -> app.decreaseVolume()
        }

        // Ensure view matched the updated app model data
        updateView()
    }

    /**
     * Key released fires when a key is lifted up
     */
    override fun keyReleased(e: KeyEvent?) {
        // Only use if you want to act on key release
    }

    /**
     * Key typed is generally for letter/number key strokes
     */
    override fun keyTyped(e: KeyEvent?) {
        // We're not generally insterested in this
    }

}

