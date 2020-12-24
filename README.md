# TinderLike Cards ♀️♂️

![Preview](/previews/swipepreview.gif)

> Stylish animations and designs like Tinder

## Why this project exists
This project arose from the idea of turning a small part of freelance project I did a long time ago into a library. With TinderLike Cards library, you can easily have a tinder-style design. Since the project was written in the past, it may have problems. I will continue to improve the library with your help. Please be sure to report any issues you find. I took the structure of listview as an example for myself. You can easily include it in your project with the use of a simple adapter.

## Features
- [x] Kotlin
- [x] Stylish Animations
- [x] Customization
- [x] CrashSafe
- [x] Easy Implementation
- [x] Application Variants

<img src="previews/left_right_button.gif" width="250"> <img src="previews/draggable_2.gif" width="250">  <img src="previews/swipe.gif" width="250">

# Let's Start :rocket:

### Define CardContainer in you main.xml

Make sure that container's layout_width and layout_height both are match_parent. I will explain it below. 🍭 

```xml
<com.asynctaskcoffee.cardstack.CardContainer
     android:id="@+id/cardContainer"
     android:layout_width="match_parent"
     android:layout_height="match_parent" />
```

### Setting CardContainerAdapter with your custom model

```kotlin
class MainAdapter(private val list: ArrayList<MainTestModel>, context: Context) :
    CardContainerAdapter() {

    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItem(position: Int) = list[position]

    @SuppressLint("InflateParams")
    override fun getView(position: Int): View {
        val v = layoutInflater.inflate(R.layout.card_view, null)
        val userImageView = v.findViewById<ImageView>(R.id.userImage)
        val genderImageView = v.findViewById<ImageView>(R.id.genderImage)
        val userName = v.findViewById<TextView>(R.id.userName)
        val ageAndLastSeen = v.findViewById<TextView>(R.id.ageAndLastSeen)

        val user = getItem(position)

        Picasso.get().load(user.userImage).into(userImageView)
        genderImageView.setImageResource(user.userGender)

        userName.text = user.userName
        ageAndLastSeen.text = user.userAgeLastSeen

        return v
    }

    override fun getCount(): Int = list.size
}
```

### Setting Adapter

```kotlin
lateinit var cardContainer: CardContainer
lateinit var adapter: MainAdapter
private var modelList = arrayListOf<MainTestModel>()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    cardContainer = findViewById(R.id.cardContainer)

    /*Setting Adapter*/
    modelList.clear()
    modelList.addAll(MainHelper.getFemaleModels())
    adapter = MainAdapter(modelList, this)
    cardContainer.setAdapter(adapter)
}
```

Almost the same as listview, right? Let's continue with customization.

### Before setting the adapter, you can set margin values and the number of visible stacks. The number of cards on the screen will be renewed according to the *maxStackSize*.

```kotlin
/*Customization*/
cardContainer.maxStackSize = 3
cardContainer.marginTop = 13.px
cardContainer.margin = 20.px
```

### So why should I define these layouts as extra ❓😿 Because in card dragging operations, parent view cordinates are taken as basis. It is necessary to do this so that the dragged card does not conflict with the parent view.

```kotlin
/*Adding Extra Layouts*/
cardContainer.setEmptyView(generateEmptyView())
cardContainer.addFooterView(generateFooterView())
cardContainer.addHeaderView(generateHeaderView())
```

### How to listen cardEvents? Here is CardListener and it's usage :tada:

```kotlin
fun onLeftSwipe(position: Int, model: Any)
fun onRightSwipe(position: Int, model: Any)
fun onItemShow(position: Int, model: Any)
fun onSwipeCancel(position: Int, model: Any)
fun onSwipeCompleted()
```

You can cast and use models according to the type you define. *Ex (model as MainTestModel)*

```kotlin
class MainActivity : AppCompatActivity(), CardListener {

   override fun onCreate(savedInstanceState: Bundle?) {
        /*Set Card Listeners*/
        cardContainer.setOnCardActionListener(this)
   }
   
    override fun onLeftSwipe(position: Int, model: Any) {/*card model shifted left*/}

    override fun onRightSwipe(position: Int, model: Any) {/*Card model shifted right*/}

    override fun onItemShow(position: Int, model: Any) {/*Current card model on screen*/}

    override fun onSwipeCancel(position: Int, model: Any) {/*If user canceled dragging*/}

    override fun onSwipeCompleted() {/*No more cards left*/}
    
}
```


### What if I have pagination system for models or need to add more cards later ❓😿 Here is the solution :tada:

```kotlin
modelList.addAll(MainHelper.getFemaleModels())
adapter.notifyAppendData()
```
⚠️ This function just for appending new datas. Do not use it for change orders or data values. ⚠️

### Can I throw cards left or right programmatically ❓🤔 Hmmm, YES YOU CAN ❗ 

You can call swipeLeft() or swipeRight() methods for it.

```kotlin
private fun setListeners(){
  cancelView.setOnClickListener {
      it.pulse() // for sweet animation
      adapter.swipeLeft()
  }
  likeView.setOnClickListener {
      it.pulse() // for sweet animation
      adapter.swipeRight()
  }
}
```
## Implementation Gradle

###### Add it in your root build.gradle at the end of repositories

```groovy
    repositories {
        maven { url 'https://jitpack.io' }
    }
```

###### Add the dependency

```groovy
    dependencies {
	     implementation 'com.github.AsynctaskCoffee:tinderlikecardstack:1.0'
	}
```

## Implementation Maven

###### Add the JitPack repository to your build file

```groovy
    <repositories>
	    <repository>
	        <id>jitpack.io</id>
	        <url>https://jitpack.io</url>
	    </repository>
    </repositories>
```

###### Add the dependency

```groovy
    <dependency>
        <groupId>com.github.AsynctaskCoffee</groupId>
        <artifactId>tinderlikecardstack</artifactId>
        <version>1.0</version>
    </dependency>
```

### Updates

> 24.12.2020 - First release


## License

```
   Copyright 2020 Egemen ÖZOGUL

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```







