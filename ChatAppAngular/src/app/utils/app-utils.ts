import { AbstractControl, ValidationErrors } from "@angular/forms";
import Swal, { SweetAlertIcon, SweetAlertPosition } from "sweetalert2";

export class AppUtils {

  public static GETCONTACT_FOR_CONTACTLIST = "CONTACTLIST";
  public static GETCONTACT_FOR_CONVERSATION = "START_CONVERSATION";
  public static GETCONTACT_FOR_GROUPLIST = "GROUPLIST";
  public static GET_MESSAGE_FOR_GROUP = "group";
  public static GET_MESSAGE_FOR_Single = "single";

  public static SEND_MESSAGE_FOR_GROUP = "group";
  public static SEND_MESSAGE_FOR_Single = "single";

  public static modelDismiss(id: any) {
    let element = document.getElementById(id);
    element?.click();
  }
  public static isEmail(control: any) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailRegex.test(control.value)) {

      return null;
    }

    return { 'invalidEmail': true };

  }

  public static isPhoneValid() {
    // const title = /^[A-Z]{1}([a-z]*\s*)+$/;
    // if (title.test(control.value)) {
    //   return null;
    // }
    // return { 'title': true };

    return (control: AbstractControl): ValidationErrors | null => {
      const title = /^[6-9]{1}[0-9]{9}$/;
      if (title.test(control.value)) {
        return null; // Valid date or no date selected
      }

      return { 'phone': true };
    };
  }
  
    // @ViewChild('emojiPickerRTE')
    // public emojiPickerRTE: RichTextEditorComponent;

    // public toolbarSettings: ToolbarSettingsModel = {
    //     items: ['Bold', 'Italic', 'Underline', '|', 'Formats', 'Alignments', 'OrderedList',
    //     'UnorderedList', '|', 'CreateLink', 'Image', '|', 'SourceCode', 'EmojiPicker', '|', 'Undo', 'Redo'
    // ]
    // };
    // public emojiPickerSettings: EmojiSettingsModel =
    // {
    //     iconsSet: [{name: 'Smilies & People', code: '1F600', iconCss: 'e-emoji', 
    //     icons: [{ code: '1F600', desc: 'Grinning face' },
    //     { code: '1F603', desc: 'Grinning face with big eyes' },
    //     { code: '1F604', desc: 'Grinning face with smiling eyes' },
    //     { code: '1F606', desc: 'Grinning squinting face' },
    //     { code: '1F605', desc: 'Grinning face with sweat' },
    //     { code: '1F602', desc: 'Face with tears of joy' },
    //     { code: '1F923', desc: 'Rolling on the floor laughing' },
    //     { code: '1F60A', desc: 'Smiling face with smiling eyes' }]
    //     }, {
    //     name: 'Animals & Nature', code: '1F435', iconCss: 'e-animals',
    //     icons: [{ code: '1F436', desc: 'Dog face' },
    //     { code: '1F431', desc: 'Cat face' },
    //     { code: '1F42D', desc: 'Mouse face' },
    //     { code: '1F439', desc: 'Hamster face' },
    //     { code: '1F430', desc: 'Rabbit face' },
    //     { code: '1F98A', desc: 'Fox face' }]
    //     }, {
    //     name: 'Food & Drink', code: '1F347', iconCss: 'e-food-and-drinks',
    //      icons: [{ code: '1F34E', desc: 'Red apple' },
    //     { code: '1F34C', desc: 'Banana' },
    //     { code: '1F347', desc: 'Grapes' },
    //     { code: '1F353', desc: 'Strawberry' },
    //     { code: '1F35E', desc: 'Bread' },
    //     { code: '1F950', desc: 'Croissant' },
    //     { code: '1F955', desc: 'Carrot' },
    //     { code: '1F354', desc: 'Hamburger' }]
    //     }, {
    //     name: 'Activities', code: '1F383', iconCss: 'e-activities',
    //     icons: [{ code: '26BD', desc: 'Soccer ball' },
    //     { code: '1F3C0', desc: 'Basketball' },
    //     { code: '1F3C8', desc: 'American football' },
    //     { code: '26BE', desc: 'Baseball' },
    //     { code: '1F3BE', desc: 'Tennis' },
    //     { code: '1F3D0', desc: 'Volleyball' },
    //     { code: '1F3C9', desc: 'Rugby football' }]
    //     }, {
    //     name: 'Travel & Places', code: '1F30D', iconCss: 'e-travel-and-places',
    //     icons: [{ code: '2708', desc: 'Airplane' },
    //     { code: '1F697', desc: 'Automobile' },
    //     { code: '1F695', desc: 'Taxi' },
    //     { code: '1F6B2', desc: 'Bicycle' },
    //     { code: '1F68C', desc: 'Bus' }]
    //     }, {
    //     name: 'Objects', code: '1F507', iconCss: 'e-objects', icons: [{ code: '1F4A1', desc: 'Light bulb' },
    //     { code: '1F526', desc: 'Flashlight' },
    //     { code: '1F4BB', desc: 'Laptop computer' },
    //     { code: '1F5A5', desc: 'Desktop computer' },
    //     { code: '1F5A8', desc: 'Printer' },
    //     { code: '1F4F7', desc: 'Camera' },
    //     { code: '1F4F8', desc: 'Camera with flash' },
    //     { code: '1F4FD', desc: 'Film projector' }]
    //     }, {
    //     name: 'Symbols', code: '1F3E7', iconCss: 'e-symbols', icons: [{ code: '274C', desc: 'Cross mark' },
    //     { code: '2714', desc: 'Check mark' },
    //     { code: '26A0', desc: 'Warning sign' },
    //     { code: '1F6AB', desc: 'Prohibited' },
    //     { code: '2139', desc: 'Information' },
    //     { code: '267B', desc: 'Recycling symbol' },
    //     { code: '1F6AD', desc: 'No smoking' }]
    //     }]
    // }


    public  static getFileIconClass(file: any): boolean {
      return file.toLowerCase().endsWith('.jpg') || file.toLowerCase().endsWith('.png') || file.toLowerCase().endsWith('.jpeg') ? true : false;
  }
  
    
public  static showMessage(icons:SweetAlertIcon='error',title='Something went wrong',position:SweetAlertPosition='top'){
    const Toast = Swal.mixin({
      toast: true,
      position: position,
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true,

    });
    Toast.fire({
      icon:icons,
      title:title
    })
  }


  public static showProcess(icon:SweetAlertIcon='error',title:string='Something went wrong'){
    Swal.fire({
        icon:icon,
        title: title,
        timer: 3000,
        timerProgressBar: true,
        didOpen: () => {
          // Swal.showLoading();
        }
      })
      
  }
  
  static min() {



    return (control: AbstractControl): ValidationErrors | null => {
      let title = control.value as string
      if (title == null) {
        return { min: false };
      }
      if (title.length >= 3) {
        return null; // Valid date or no date selected
      }



      return { min: true }; // Sunday is not allowed
    };
  }
  static max() {
    return (control: AbstractControl): ValidationErrors | null => {
      let title = control.value as string
      if (title == null) {
        return { max: false };
      } if (title.length <= 50) {
        return null; // Valid date or no date selected
      }
      return { max: true }; // Sunday is not allowed
    };
  }

}



;
