import com.thoughtworks.binding.dom

/**
  * Created by stanikol on 11/2/16.
  */
object NavigationBar {

  @dom
  def navigationBar = {
    <!-- Dropdown Structure -->
    <nav>
      <ul data:id="dropdown1" class="dropdown-content">
        <li><a href="#goods" >Витрина</a></li>
        <li><a href="#about">О нас</a></li>
        <li class="divider"></li>
        <li><a href="#Рецепты">Рецепты</a></li>
        <li><a href="#blog">Блог</a></li>
        <li class="divider"></li>
        <li><a href="#Всем мира и добра ! ;)" >Всем мира и добра ! ;)</a></li>
      </ul>
      <div class="nav-wrapper ">
        <a href="#" class="brand-logo center">Doux Doux Joux Joux Macorons</a>
        <ul class="right hide-on-small-only">
          <li><a href="#" >Витрина</a></li>
          <li><a href="#О нас">О нас</a></li>
          <!-- Dropdown Trigger -->
          <li><a class="dropdown-button" href="#!" data:data-activates="dropdown1">
            Меню<i class="material-icons right">arrow_drop_down</i></a>
          </li>
        </ul>
      </div>

    </nav>
      <button data:data-activates="slide-out" class="button-collapse hide-on-med-and-up"><i class="material-icons">menu</i></button>
      <ul data:id="slide-out" class="side-nav">
        <li><a href="#about">О нас</a></li>
        <li class="divider"></li>
        <li><a href="#goods" >Витрина</a></li>
        <li><a href="#Рецепты">Рецепты</a></li>
        <li><a href="#blog">Блог</a></li>
        <li class="divider"></li>
        <li><a href="#" >Витрина</a></li>
        <li><a href="#Всем мира и добра ! ;)" >Всем мира и добра ! ;)</a></li>
      </ul>
  }


}
