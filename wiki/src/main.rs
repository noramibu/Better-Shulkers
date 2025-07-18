use image::ImageFormat;
use slint::{Image, Rgba8Pixel, SharedPixelBuffer};
use std::collections::HashMap;
#[cfg(target_family = "wasm")]
use wasm_bindgen::prelude::*;
use web_sys::window;

slint::include_modules!();

#[cfg_attr(target_family = "wasm", wasm_bindgen(start))]
pub fn main() {
    let site = Site::new().unwrap();
    let gifs = load_gifs();
    
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
                window.location().set_href("https://discord.gg/XGw3Te7QYr").expect("failed to redirect");
            }
        }
    });

    site.global::<Utility>().on_load_gif_frame(move |name, frame| {
        let frames = gifs.get(name.as_str()).unwrap();
        let bytes = frames[frame as usize];
        let buffer = read_png(&bytes);
        Image::from_rgba8(buffer)
    });
    
    site.run().unwrap();
}

fn load_gifs() -> HashMap<String, Vec<&'static [u8]>> {
    let mut gifs_map: HashMap<String, Vec<&'static [u8]>> = HashMap::new();

    gifs_map.insert(
        "pickup".to_string(),
        vec![
            include_bytes!("../ui/gifs/pickup/4.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/5.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/6.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/7.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/8.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/9.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/10.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/11.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/12.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/13.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/14.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/15.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/16.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/17.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/18.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/19.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/20.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/22.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/23.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/24.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/25.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/26.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/27.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/28.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/29.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/30.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/31.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/32.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/33.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/34.png") as &[u8],
            include_bytes!("../ui/gifs/pickup/35.png") as &[u8],
        ],
    );

    gifs_map.insert(
        "shulker_open".to_string(),
        vec![
            include_bytes!("../ui/gifs/shulker_open/1.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/2.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/3.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/4.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/5.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/6.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/7.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/8.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/9.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/10.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/11.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/19.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/20.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/21.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/22.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/23.png") as &[u8],
            include_bytes!("../ui/gifs/shulker_open/24.png") as &[u8],
        ],
    );

    gifs_map.insert(
        "spin".to_string(),
        vec![
            include_bytes!("../ui/gifs/spin/1.png") as &[u8],
            include_bytes!("../ui/gifs/spin/2.png") as &[u8],
            include_bytes!("../ui/gifs/spin/3.png") as &[u8],
            include_bytes!("../ui/gifs/spin/4.png") as &[u8],
            include_bytes!("../ui/gifs/spin/5.png") as &[u8],
            include_bytes!("../ui/gifs/spin/6.png") as &[u8],
            include_bytes!("../ui/gifs/spin/7.png") as &[u8],
            include_bytes!("../ui/gifs/spin/8.png") as &[u8],
            include_bytes!("../ui/gifs/spin/9.png") as &[u8],
            include_bytes!("../ui/gifs/spin/10.png") as &[u8],
            include_bytes!("../ui/gifs/spin/11.png") as &[u8],
            include_bytes!("../ui/gifs/spin/12.png") as &[u8],
            include_bytes!("../ui/gifs/spin/13.png") as &[u8],
            include_bytes!("../ui/gifs/spin/14.png") as &[u8],
            include_bytes!("../ui/gifs/spin/15.png") as &[u8],
            include_bytes!("../ui/gifs/spin/16.png") as &[u8],
            include_bytes!("../ui/gifs/spin/17.png") as &[u8],
            include_bytes!("../ui/gifs/spin/18.png") as &[u8],
            include_bytes!("../ui/gifs/spin/19.png") as &[u8],
            include_bytes!("../ui/gifs/spin/20.png") as &[u8],
            include_bytes!("../ui/gifs/spin/21.png") as &[u8],
            include_bytes!("../ui/gifs/spin/22.png") as &[u8],
            include_bytes!("../ui/gifs/spin/23.png") as &[u8],
            include_bytes!("../ui/gifs/spin/24.png") as &[u8],
            include_bytes!("../ui/gifs/spin/25.png") as &[u8],
            include_bytes!("../ui/gifs/spin/26.png") as &[u8],
            include_bytes!("../ui/gifs/spin/27.png") as &[u8],
            include_bytes!("../ui/gifs/spin/28.png") as &[u8],
            include_bytes!("../ui/gifs/spin/29.png") as &[u8],
            include_bytes!("../ui/gifs/spin/30.png") as &[u8],
            include_bytes!("../ui/gifs/spin/31.png") as &[u8],
            include_bytes!("../ui/gifs/spin/32.png") as &[u8],
            include_bytes!("../ui/gifs/spin/33.png") as &[u8],
            include_bytes!("../ui/gifs/spin/34.png") as &[u8],
            include_bytes!("../ui/gifs/spin/35.png") as &[u8],
            include_bytes!("../ui/gifs/spin/36.png") as &[u8],
        ],
    );

    gifs_map
}

fn read_png(bytes: &[u8]) -> SharedPixelBuffer<Rgba8Pixel> {
    let image = image::load_from_memory_with_format(&bytes, ImageFormat::PNG).expect("failed to load png data").to_rgba();
    let width = image.width();
    let height = image.height();
    
    let buffer = SharedPixelBuffer::<Rgba8Pixel>::clone_from_slice(
        &*image.into_raw(),
        width,
        height,
    );

    buffer
}
