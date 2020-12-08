package com.asynctaskcoffee.tinderlikecardstack.ui

import com.asynctaskcoffee.tinderlikecardstack.R
import kotlin.collections.ArrayList

class MainHelper {
    companion object Male {
        fun getMaleModels(): ArrayList<MainTestModel> {
            val list = arrayListOf<MainTestModel>()
            list.add(MainTestModel("Oliver Jake", "https://randomuser.me/api/portraits/men/75.jpg", R.drawable.icons_2_dp_male_2_dp, "25 - Last seen 50m ago"))
            list.add(MainTestModel("Jack Reece", "https://randomuser.me/api/portraits/men/76.jpg", R.drawable.icons_2_dp_male_2_dp, "24 - Last seen 27m ago"))
            list.add(MainTestModel("Harry Connor", "https://randomuser.me/api/portraits/men/77.jpg", R.drawable.icons_2_dp_male_2_dp, "32 - Last seen 55m ago"))
            list.add(MainTestModel("Jacob Michael", "https://randomuser.me/api/portraits/men/78.jpg", R.drawable.icons_2_dp_male_2_dp, "29 - Last seen 2m ago"))
            list.add(MainTestModel("Charlie Kyle", "https://randomuser.me/api/portraits/men/79.jpg", R.drawable.icons_2_dp_male_2_dp, "20 - Last seen 8m ago"))
            list.add(MainTestModel("Thomas Joe", "https://randomuser.me/api/portraits/men/80.jpg", R.drawable.icons_2_dp_male_2_dp, "28- Last seen 30m ago"))
            return list
        }

        fun getFemaleModels(): ArrayList<MainTestModel> {
            val list = arrayListOf<MainTestModel>()
            list.add(MainTestModel("Olivia Amelia", "https://randomuser.me/api/portraits/women/75.jpg", R.drawable.icons_2_dp_female_2_dp, "25 - Last seen 50m ago"))
            list.add(MainTestModel("Emma Mia", "https://randomuser.me/api/portraits/women/76.jpg", R.drawable.icons_2_dp_female_2_dp, "24 - Last seen 27m ago"))
            list.add(MainTestModel("Ava Harper", "https://randomuser.me/api/portraits/women/77.jpg", R.drawable.icons_2_dp_female_2_dp, "32 - Last seen 55m ago"))
            list.add(MainTestModel("Sophia Evelyn", "https://randomuser.me/api/portraits/women/78.jpg", R.drawable.icons_2_dp_female_2_dp, "29 - Last seen 2m ago"))
            list.add(MainTestModel("Isabella Abigail", "https://randomuser.me/api/portraits/women/79.jpg", R.drawable.icons_2_dp_female_2_dp, "20 - Last seen 8m ago"))
            list.add(MainTestModel("Charlotte Emily", "https://randomuser.me/api/portraits/women/80.jpg", R.drawable.icons_2_dp_female_2_dp, "28- Last seen 30m ago"))
            return list
        }

        fun getAllModels(): ArrayList<MainTestModel> {
            val list = arrayListOf<MainTestModel>()
            list.addAll(getFemaleModels())
            list.addAll(getMaleModels())
            list.shuffle()
            return list
        }
    }

}