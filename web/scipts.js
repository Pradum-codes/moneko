/* ── Mobile menu toggle ── */
const menuBtn = document.getElementById("menuBtn");
const nav = document.getElementById("nav");

if (menuBtn) {
  menuBtn.addEventListener("click", () => nav.classList.toggle("open"));
}

/* ── Smooth scroll for nav links ── */
document.querySelectorAll("[data-scroll]").forEach((link) => {
  link.addEventListener("click", (e) => {
    e.preventDefault();
    const target = document.querySelector(link.getAttribute("href"));
    if (target) {
      target.scrollIntoView({ behavior: "smooth", block: "start" });
      nav.classList.remove("open");
    }
  });
});

/* ── Smooth scroll for buttons ── */
document.querySelectorAll("[data-scroll-target]").forEach((btn) => {
  btn.addEventListener("click", () => {
    const target = document.querySelector(btn.getAttribute("data-scroll-target"));
    if (target) {
      target.scrollIntoView({ behavior: "smooth", block: "start" });
      nav.classList.remove("open");
    }
  });
});

/* ── Scroll-reveal ── */
const revealObserver = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible");
        revealObserver.unobserve(entry.target);
      }
    });
  },
  { threshold: 0.12 }
);

document.querySelectorAll("[data-reveal]").forEach((el) => revealObserver.observe(el));

/* ── CTA form handler ── */
const ctaForm = document.querySelector(".cta-form");
if (ctaForm) {
  ctaForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const input = ctaForm.querySelector("input");
    const button = ctaForm.querySelector("button");
    if (!input.value) return;
    button.textContent = "You're in!";
    button.disabled = true;
    input.value = "";
    setTimeout(() => {
      button.textContent = "Notify Me";
      button.disabled = false;
    }, 2500);
  });
}
