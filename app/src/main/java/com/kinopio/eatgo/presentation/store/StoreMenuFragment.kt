import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kinopio.eatgo.R
import com.kinopio.eatgo.domain.store.ui_model.Menu
import com.kinopio.eatgo.presentation.store.BestMenuAdapter
import com.kinopio.eatgo.presentation.store.MenuAdapter

class StoreMenuFragment : Fragment() {
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var bestMenuAdapter: BestMenuAdapter

    private val menuList = mutableListOf<Menu>()
    private val bestMenuList = mutableListOf<Menu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_store_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터 전달 받기
        val menuList = arguments?.getSerializable(ARG_MENU_LIST) as? List<Menu>
        if (menuList != null) {
            this.menuList.addAll(menuList)
            this.bestMenuList.addAll(menuList)
            // menuList 데이터 활용
        }

        // Best Menu RecyclerView 설정
        val bestMenurecyclerView: RecyclerView = view.findViewById(R.id.best_menu_rv)
        bestMenurecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bestMenuAdapter = BestMenuAdapter(bestMenuList)
        bestMenurecyclerView.adapter = bestMenuAdapter


        // Menu RecyclerView 설정
        val recyclerView: RecyclerView = view.findViewById(R.id.menu_rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        menuAdapter = MenuAdapter(menuList)
        recyclerView.adapter = menuAdapter
    }

    companion object {
        private const val ARG_MENU_LIST = "menu_list"

        fun newInstance(menuList: List<Menu>): StoreMenuFragment {
            val fragment = StoreMenuFragment()
            val args = Bundle()
            args.putSerializable(ARG_MENU_LIST, ArrayList(menuList))
            fragment.arguments = args
            return fragment
        }
    }
}
