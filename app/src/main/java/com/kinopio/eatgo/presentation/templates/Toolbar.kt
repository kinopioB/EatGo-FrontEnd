import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.kinopio.eatgo.R

class ToolbarUtils {
    companion object {
        private lateinit var toolbarBodyTemplate: Toolbar

        fun setupToolbar(activity: AppCompatActivity, toolbar: Toolbar, title: String, categoryId: Int?) {
            toolbarBodyTemplate = toolbar
            val titleTextView = toolbar.findViewById<TextView>(R.id.title)
            val iconImageView = toolbar.findViewById<ImageView>(R.id.image)
            val categoryName = "category_$categoryId"

            // TextView 사용
            if(categoryId == null){
                iconImageView.visibility = View.GONE
                titleTextView.text = title
            }else{
                titleTextView.visibility = View.GONE
                iconImageView.setImageDrawable(ContextCompat.getDrawable(toolbar.context, activity.resources.getIdentifier(categoryName, "drawable", activity.packageName)))
            }
            activity.setSupportActionBar(toolbarBodyTemplate)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }



        fun handleOptionsItemSelected(activity: AppCompatActivity, item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> {
                    activity.finish()
                    return true
                }
            }
            return false
        }
    }
}
