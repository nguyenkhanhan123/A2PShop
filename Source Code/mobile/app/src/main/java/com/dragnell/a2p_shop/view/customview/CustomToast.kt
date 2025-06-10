import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.dragnell.a2p_shop.R
import com.dragnell.a2p_shop.databinding.CustomToastBinding

object CustomToast {

    fun showToast(context: Context, message: String, type: ToastType) {
        val mbinding = CustomToastBinding.inflate(LayoutInflater.from(context))

        mbinding.toastText.text = message

        when (type) {
            ToastType.SUCCESS -> {
                mbinding.toastIcon.setImageResource(R.drawable.ic_sucess)
                mbinding.toastIndicator.setBackgroundColor(Color.GREEN)
                mbinding.toastText.setTextColor(Color.BLACK)
            }
            ToastType.ERROR -> {
                mbinding.toastIcon.setImageResource(R.drawable.ic_eror)
                mbinding.toastIndicator.setBackgroundColor(Color.RED)
                mbinding.toastText.setTextColor(Color.BLACK)
            }
            ToastType.WARNING -> {
                mbinding.toastIcon.setImageResource(R.drawable.ic_warning)
                mbinding.toastIndicator.setBackgroundColor(Color.YELLOW)
                mbinding.toastText.setTextColor(Color.BLACK)
            }
            ToastType.INFO -> {
                mbinding.toastIcon.setImageResource(R.drawable.ic_info)
                mbinding.toastIndicator.setBackgroundColor(Color.BLUE)
                mbinding.toastText.setTextColor(Color.BLACK)
            }
        }

        val toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.view = mbinding.root
        toast.show()
    }

    enum class ToastType {
        SUCCESS, ERROR, WARNING, INFO
    }
}
