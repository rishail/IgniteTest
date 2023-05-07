import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.ignitetest.DBHelper
import com.example.ignitetest.OnDialogClickListener
import com.example.ignitetest.R
import com.example.ignitetest.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddTODOTASK : BottomSheetDialogFragment() {


    private var mEditText: EditText? = null
    private var mSaveButton: Button? = null
    private var myDb: DBHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_to_do_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mEditText = view.findViewById(R.id.edittext)
        mSaveButton = view.findViewById(R.id.button_save)
        myDb = activity?.let { DBHelper(it) }
        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            mEditText?.setText(task)
            if (task!!.length > 0) {
                mSaveButton?.isEnabled = false
            }
        }
        mEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() == "") {
                    mSaveButton?.setEnabled(false)
                    mSaveButton?.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveButton?.setEnabled(true)
                    mSaveButton?.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val finalIsUpdate = isUpdate
        mSaveButton?.setOnClickListener {
            val text = mEditText?.text.toString()
            if (finalIsUpdate) {
                myDb?.updateTask(bundle!!.getInt("id"), text)
            } else {
                val item = Todo()
                item.task=text
                item.status=0
                myDb?.insertTask(item)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is OnDialogClickListener) {
            (activity as OnDialogClickListener?)?.onDialogClose(dialog)
        }
    }

    companion object {
        const val TAG = "AddNewTask"
        fun newInstance(): AddTODOTASK {
            return AddTODOTASK()
        }
    }
}
