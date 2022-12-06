package com.educards.adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.model.entities.Card
<<<<<<< Updated upstream

class RecyclerCardAdapter(private var cards: MutableList<Card>, private var _btReverse:ImageButton, private var _context:Context, private val activity: Activity?) :
=======
import com.educards.service.SCard
import com.educards.util.UAlertGenericDialog.createDialogAlert
import com.educards.util.UTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.collection.LLRBNode.Color
import com.google.firebase.firestore.model.Values
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerCardAdapter(
    private var cards: MutableList<Card>,
    private var _btReverse:ImageButton,
    private var _context:Context,
    private val activity: Activity?,
    private val recyclerCard:RecyclerView) :
>>>>>>> Stashed changes
    RecyclerView.Adapter<RecyclerCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, item: Int): ViewHolder {
        val holder = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)
        return ViewHolder(holder)
    }

<<<<<<< Updated upstream
=======
   //@SuppressLint("ClickableViewAccessibility")
   var currentPositionCard = 0

    var isReScrolled = false
    var positionCardScrolled = 0
    var isAnswer = false
>>>>>>> Stashed changes
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
        when(position){
            //al primer item se le agrega el evento de click para que no tenga que tocar la carta para girarla
            0->{
                holder.cardAnimation()
            }
            //cuando se agrega una pregunta se va a la ultima y se le agrega el click para que pueda girarla
            //sin tocarla
            cards.size-1 ->{
                holder.cardAnimation()
            }
        }

        //holder.itemView.setOnClickListener { holder.cardAnimation()}
        holder.itemView.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                holder.cardAnimation()
                return true
            }
        })

        //comportamiento cuando se ha redesplazado a una posicion por agregar/actualizar/eliminar una tarjeta
        if (isReScrolled && (position==positionCardScrolled)){
            //se activa la animacion directamente solo al item donde se ha scrolleado
            holder.cardAnimation()
            //si el item donde se dezplaza se causo porque se edito una respuesta,se obliga a girar a la tarjeta
            if (isAnswer){
                _btReverse.performClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            private var question: TextView
            private var answer: TextView
            private var cardQuestion: CardView?
            private var cardAnswer: CardView?
            private lateinit var dialog: AlertDialog

            private var editQuestion: EditText
            private var editAnswer: EditText

            private lateinit var frontAnim: AnimatorSet
            private lateinit var backAnim: AnimatorSet
            private var isFront = true

            init {
                question = view.findViewById(R.id.et_question)
                answer = view.findViewById(R.id.et_answer)
                cardQuestion = view.findViewById(R.id.cv_question)
                cardAnswer = view.findViewById(R.id.cv_answer)
                editQuestion = view.findViewById(R.id.et_card_question)
                editAnswer = view.findViewById(R.id.et_card_answer)

            }

            fun bind(card: Card){
                question.text = card.getQuestion()
                answer.text = card.getAnswer()

                question.setOnClickListener {
<<<<<<< Updated upstream
=======
                    viewCard = activity?.layoutInflater?.inflate(R.layout.dialog_input, null)
                    dialogItems(viewCard)
                    cardQuestion?.isEnabled
                    cardAnswer?.isEnabled = false
                    question.selectionStart
                    val builder = AlertDialog.Builder(_context)
                    tvEditCardTitle.hint = _context.getString(R.string.dialog_buttom_question)
                    cardHeader.text = "Add question"
                    tvEditCard.setText(card.getQuestion())
                    builder.setView(viewCard)

                    dialog = builder.create()
                    dialog.show()

                    dialog.setCanceledOnTouchOutside(true)

                    currentPositionCard = this.adapterPosition
                    addCard.setOnClickListener {
                        if (UTextView.verifyContentInEditText(_context,tvEditCard,"Null or empty question field")) {
                            //la llave la rama padre de la tarjeta fue guardada al dar click en el mazo
                            if (tvEditCard.text.toString().length < 375) {
                                SCard.updateCard(_context, Card(card.getId(), tvEditCard.text.toString(), card.getAnswer()), "pregunta")

                                //re dezplazar donde estaba este item
                                isAnswer = false
                                returnToPosition(currentPositionCard)

                                dialog.dismiss()
                            }else{
                                createDialogAlert(_context,"Answer","Due to design and readability of the text, it is not allowed to save a question with more than 375 characters")
                            }
                        }
                    }
                }

                answer.setOnClickListener {
                    viewCard = activity?.layoutInflater?.inflate(R.layout.dialog_input, null)
                    dialogItems(viewCard)
                    cardAnswer?.isEnabled
                    cardQuestion?.isEnabled = false
                    answer.selectionStart
>>>>>>> Stashed changes
                    val builder = AlertDialog.Builder(_context)
                    val viewDialog = activity?.layoutInflater?.inflate(R.layout.dialog_input_question, null)
                    builder.setView(viewDialog)

                    dialog = builder.create()
                    dialog.show()
<<<<<<< Updated upstream
                }
            }
=======

                    dialog.setCanceledOnTouchOutside(true)

                    currentPositionCard = this.adapterPosition
                    addCard.setOnClickListener {
                        if (UTextView.verifyContentInEditText(_context,tvEditCard,"Null or empty response field")) {
                            //la llave la rama padre de la tarjeta fue guardada al dar click en el mazo
                            if (tvEditCard.text.toString().length < 375) {
                                SCard.updateCard(_context, Card(card.getId(), card.getQuestion(), tvEditCard.text.toString()), "respuesta")

                                //re dezplazar donde estaba este item
                                isAnswer = true
                                returnToPosition(currentPositionCard)

                                dialog.dismiss()
                            }else{
                                createDialogAlert(_context,"Answer","Due to design and readability of the text, it is not allowed to save a response with more than 375 characters")
                            }
                        }
                    }
                }

                deleteCard.setOnClickListener{
                    currentPositionCard = this.adapterPosition
                    builder.setTitle("Confirm delete")
                        .setMessage("\nDo you want to remove card?")
                        .setCancelable(true)
                        .setPositiveButton("Yes"){dialogInterface, it ->
                            //la llave la rama padre de la tarjeta fue guardada al dar click en el mazo
                            SCard.deleteCard(_context,card.getId())

                            //re desplazar a la posicion-1 del donde estaba este item
                            if (currentPositionCard == 0) {
                                returnToPosition(0)
                            }else {
                                returnToPosition(currentPositionCard - 1)
                            }
                        }
                        .setNegativeButton("No"){dialogInterface, it ->
                            dialogInterface.cancel()
                        }
                        .show()
                }

            }

        private fun returnToPosition(_position:Int){
            isReScrolled = true
            positionCardScrolled = _position
            CoroutineScope(Dispatchers.Main).launch {
                recyclerCard.scrollToPosition(_position)
                currentPositionCard = _position

            }
        }
        fun dialogItems(_viewCard:View?){
            tvEditCardTitle = _viewCard!!.findViewById(R.id.titl_title)
            tvEditCard = _viewCard!!.findViewById(R.id.et_title)
            cardHeader = _viewCard!!.findViewById(R.id.tv_header)
            addCard = _viewCard!!.findViewById(R.id.bt_create)
        }

        private lateinit var frontAnim: AnimatorSet
        private lateinit var backAnim: AnimatorSet
        private var isFront = true
>>>>>>> Stashed changes

            fun cardAnimation(){
                val scale = _context.resources.displayMetrics.density
                cardQuestion?.cameraDistance = 8000 * scale
                cardAnswer?.cameraDistance = 8000 * scale

                frontAnim = AnimatorInflater.loadAnimator(_context, R.animator.front_animation) as AnimatorSet
                backAnim = AnimatorInflater.loadAnimator(_context, R.animator.back_animation) as AnimatorSet

                //quita sonido del boton si se redezplaza a una respuesta
                _btReverse.isSoundEffectsEnabled = !isAnswer

                _btReverse.setOnClickListener{
<<<<<<< Updated upstream
                    if(isFront){
                        frontAnim.setTarget(cardQuestion)
                        backAnim.setTarget(cardAnswer)
                        frontAnim.start()
                        backAnim.start()

                        isFront = false
                    } else{
                        frontAnim.setTarget(cardAnswer)
                        backAnim.setTarget(cardQuestion)
                        backAnim.start()
                        frontAnim.start()

                        isFront = true
                    }
                }
            }
=======
                    //reactiva el sonido
                    _btReverse.isSoundEffectsEnabled = true
                    //si ha sido redesplazado por editar una respuesta se activa la animacion con una duracion menor
                    //para evitar que se dectecte el giro
                    if (isAnswer) {
                        //a mayor magnitud mas veloz
                        frontAnim.currentPlayTime= 1000L
                        backAnim.currentPlayTime = 1000L
                        //la animacion rapida solo debe suceder una vez
                        activeAnimation()
                        isAnswer = false
                    }else{
                        frontAnim.currentPlayTime = 1L
                        backAnim.currentPlayTime = 1L
                        activeAnimation()
                    }
                }
            }
        private fun activeAnimation(){
            if(isFront){
                cardAnswer?.visibility = View.VISIBLE
                frontAnim.setTarget(cardQuestion)
                backAnim.setTarget(cardAnswer)
                frontAnim.start()
                backAnim.start()

                isFront = false
            } else{
                cardAnswer?.visibility = View.GONE
                frontAnim.setTarget(cardAnswer)
                backAnim.setTarget(cardQuestion)
                backAnim.start()
                frontAnim.start()

                isFront = true
            }
        }

>>>>>>> Stashed changes
    }
}