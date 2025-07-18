#[cfg(target_family = "wasm")]
use wasm_bindgen::prelude::*;
use web_sys::window;

slint::include_modules!();

#[cfg_attr(target_family = "wasm", wasm_bindgen(start))]
pub fn main() {
    let site = Site::new().unwrap();
    
    site.global::<Utility>().on_redirect(move |redirect| {
        let window = window().expect("no global `window` exists");
        match redirect {
            Redirect::Modrinth => {
                window.location().set_href("https://modrinth.com/mod/better-shulkers").expect("failed to redirect");
            }
            Redirect::Curseforge => {
                window.location().set_href("https://curseforge.com").expect("failed to redirect");
            }
            Redirect::Github => {
                window.location().set_href("https://github.com/noramibu/Better-Shulkers").expect("failed to redirect");
            }
            Redirect::Discord => {
                window.location().set_href("https://discord.com").expect("failed to redirect");
            }
        }
    });
    
    site.run().unwrap();
}
