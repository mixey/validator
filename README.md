## What is Validator? 
**BindViewValidator** annotation allows you to provide for field an extra feature such as validation. **BindViewValidator** has some settings: 
* type - Validator type (EMAIL, PASSWORD, PHONE, CHECK, REQUIRED, NUMBER, NAME, CUSTOM).
* hasLayout - Flag that shows that this field has TextInputLayout and error will output into one.
* errorMessage - Some validator has a default error message but you can override it.
* related - String value where you can set a related field. Works with PassowrdValidator only.
* pattern - Pattern for RegEx. Using for CUSTOM type validation.

## Usage 

1) Snippet.xml
``` XML
<android.support.design.widget.TextInputLayout
	android:layout_width="match_parent"
	android:layout_height="wrap_content">

	<EditText
		android:id="@+id/text_field"
		android:layout_width="match_parent"
		android:layout_height="wrap_content"
		android:ems="10"
		android:inputType="textPersonName" />
</android.support.design.widget.TextInputLayout>

```
2) Snippet.java
``` Java
class AnyView {

@BindViewValidator(type = ValidatorType.NUMBER, hasLayout = true)
@BindView(R.id.text_field)
EditText textView;

private IBindValidator bindValidator;

 @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindValidator = BindValidator.bind(this);

        // example validator usage 
        ValidatorGroup v = bindValidator.getValidator(); 
        if (!v.validate()) {
            bindValidator.setError(v.getErrors());            
        }
    }
}
```

## Examples
``` Java
@BindViewValidator(type = ValidatorType.EMAIL, field = "email")
@BindView(R.id.text_email)
EditText fieldView;
...
@BindViewValidator(type = ValidatorType.PASSWORD, related = "password2View")
@BindView(R.id.text_password)
EditText passwordView;

@BindView(R.id.text_password2)
EditText password2View;
...

@BindViewValidator(type = ValidatorType.PHONE)
@BindView(R.id.text_phone)
EditText phoneView;
```
## How to add a custom view into Validator
1) You need implement IFieldExtension in your custom view 
2) Add your custom view manualy **bindValidator.add(<your_custom_view>);**

## How to add in project 
```
allprojects {
  repositories {
	...
	maven { url 'https://jitpack.io' }
	}
}

dependencies {
    compile 'com.github.mixey:validator:1.0'
}
```
