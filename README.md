
# 🌟 Doraemon Mahjong

<img src="https://github.com/haechan29/Mahjong/assets/63138511/fca21d55-99ad-44aa-a5e6-af6350691403" style="width:300px"></img>

</br>

# 🌈 Reponsibilities
## Card
- Simple data class that conists of ``idx``, ``number``
- ``idx`` determines __the place__ in the layout
- ``number`` determines __the image__ of the front side

</br>

## CardMatcher
- Check ``matching`` when put two cards in it
- Internally checks whether ``number`` is the same while ``idx`` is different

</br>

## MahjongCardState
- __Data class__ to save the state of MahjongCard
- Consists of three variables: ``rotatedState``, ``blurred``, ``clickable``
- When two cards are put to the card matcher, </br>if __matching__, card becomes ``blurred`` and ``not clickable``, if __not matching__, just ``rotated`` again.

</br>

# 🛠️ Skills
## Jetpack Compose
- Separated the __state__ and the __layout__ (``MahjongCardState`` - ``MahjongCard``)
- Applied __rotation__, __alpha animation__ using ``graphics layer``
