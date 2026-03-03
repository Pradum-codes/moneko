const schema=[
  {g:'Brand Colors',k:'--primary',l:'Primary',d:'#6C63FF'},
  {g:'Brand Colors',k:'--secondary',l:'Secondary',d:'#FF6584'},
  {g:'Brand Colors',k:'--accent',l:'Accent',d:'#43E97B'},
  {g:'Backgrounds',k:'--background',l:'Background',d:'#F5F4FF'},
  {g:'Backgrounds',k:'--surface',l:'Surface',d:'#FFFFFF'},
  {g:'Backgrounds',k:'--surface2',l:'Surface 2',d:'#F0EFFE'},
  {g:'Typography',k:'--text-primary',l:'Text Primary',d:'#1A1A2E'},
  {g:'Typography',k:'--text-secondary',l:'Text Secondary',d:'#6B6B8A'},
  {g:'Typography',k:'--text-on-primary',l:'On Primary',d:'#FFFFFF'},
  {g:'Typography',k:'--text-on-secondary',l:'On Secondary',d:'#FFFFFF'},
  {g:'States',k:'--error',l:'Error',d:'#FF4D6D'},
  {g:'States',k:'--success',l:'Success',d:'#43E97B'},
  {g:'States',k:'--warning',l:'Warning',d:'#FFD166'},
  {g:'UI Elements',k:'--border',l:'Border',d:'#E0DEFF'},
];
const presets={
  default:{'--primary':'#6C63FF','--secondary':'#FF6584','--accent':'#43E97B','--background':'#F5F4FF','--surface':'#FFFFFF','--surface2':'#F0EFFE','--text-primary':'#1A1A2E','--text-secondary':'#6B6B8A','--text-on-primary':'#FFFFFF','--text-on-secondary':'#FFFFFF','--error':'#FF4D6D','--success':'#43E97B','--warning':'#FFD166','--border':'#E0DEFF'},
  ocean:{'--primary':'#0077B6','--secondary':'#48CAE4','--accent':'#90E0EF','--background':'#E8F4FD','--surface':'#FFFFFF','--surface2':'#CAF0F8','--text-primary':'#03045E','--text-secondary':'#5A6C8A','--text-on-primary':'#FFFFFF','--text-on-secondary':'#03045E','--error':'#EF233C','--success':'#06D6A0','--warning':'#FFD166','--border':'#ADE8F4'},
  sunset:{'--primary':'#F72585','--secondary':'#7209B7','--accent':'#FFBE0B','--background':'#FFF0F5','--surface':'#FFFFFF','--surface2':'#FFE5F0','--text-primary':'#2B0D3A','--text-secondary':'#8A4870','--text-on-primary':'#FFFFFF','--text-on-secondary':'#FFFFFF','--error':'#EF233C','--success':'#06D6A0','--warning':'#FFBE0B','--border':'#FFB3CE'},
  forest:{'--primary':'#2D6A4F','--secondary':'#52B788','--accent':'#95D5B2','--background':'#EEF7F2','--surface':'#FFFFFF','--surface2':'#D8F3DC','--text-primary':'#1B2E22','--text-secondary':'#4A7C59','--text-on-primary':'#FFFFFF','--text-on-secondary':'#1B2E22','--error':'#D62828','--success':'#52B788','--warning':'#E9C46A','--border':'#B7E4C7'},
  midnight:{'--primary':'#7B2FBE','--secondary':'#E040FB','--accent':'#03DAC6','--background':'#0D0D1A','--surface':'#1A1A2E','--surface2':'#252540','--text-primary':'#E8E6FF','--text-secondary':'#8080B0','--text-on-primary':'#FFFFFF','--text-on-secondary':'#FFFFFF','--error':'#CF6679','--success':'#03DAC6','--warning':'#FFD166','--border':'#333360'},
  rose:{'--primary':'#E63946','--secondary':'#FF8FA3','--accent':'#FFB3C1','--background':'#FFF5F6','--surface':'#FFFFFF','--surface2':'#FFECEE','--text-primary':'#2D1B1E','--text-secondary':'#8A4A50','--text-on-primary':'#FFFFFF','--text-on-secondary':'#FFFFFF','--error':'#E63946','--success':'#2A9D8F','--warning':'#E9C46A','--border':'#FFD6DA'},
  amber:{'--primary':'#FB8500','--secondary':'#FFB703','--accent':'#219EBC','--background':'#FFF8EC','--surface':'#FFFFFF','--surface2':'#FFF3D6','--text-primary':'#212A36','--text-secondary':'#7A6040','--text-on-primary':'#FFFFFF','--text-on-secondary':'#212A36','--error':'#D62828','--success':'#2A9D8F','--warning':'#FFB703','--border':'#FFD980'},
  mono:{'--primary':'#1A1A1A','--secondary':'#555555','--accent':'#888888','--background':'#F8F8F8','--surface':'#FFFFFF','--surface2':'#F0F0F0','--text-primary':'#111111','--text-secondary':'#777777','--text-on-primary':'#FFFFFF','--text-on-secondary':'#FFFFFF','--error':'#CC0000','--success':'#006600','--warning':'#996600','--border':'#DDDDDD'},
};
let cur={...presets.default};
function showToast(msg){
  const t=document.getElementById('toast');
  if(!t)return;
  t.textContent=msg;
  t.classList.add('show');
  setTimeout(()=>t.classList.remove('show'),2200);
}
function buildControls(){
  const panel=document.getElementById('lp');
  const groups={};
  schema.forEach(i=>{if(!groups[i.g])groups[i.g]=[];groups[i.g].push(i)});
  Object.entries(groups).forEach(([g,items])=>{
    const t=document.createElement('div');t.className='sec';t.textContent=g;panel.appendChild(t);
    items.forEach(item=>{
      const hex=cur[item.k]||item.d;const sk=item.k.replace(/-/g,'_');
      const row=document.createElement('div');row.className='crow';row.id='row'+sk;
      row.innerHTML=`<div class="sw" title="Click to pick color"><div class="sp" id="sp${sk}" style="background:${hex}"></div><input type="color" id="pk${sk}" value="${hex}" oninput="onP('${item.k}','${sk}',this.value)" onchange="onP('${item.k}','${sk}',this.value)"></div><div class="ci"><span class="cl">${item.l}</span><input class="ch" id="hx${sk}" type="text" value="${hex}" maxlength="9" oninput="onH('${item.k}','${sk}',this.value)" onblur="onHB('${item.k}','${sk}',this)"></div>`;
      panel.appendChild(row);
    });
  });
}
function onP(k,sk,v){cur[k]=v;document.getElementById('hx'+sk).value=v;applyColors()}
function onH(k,sk,v){const c=v.startsWith('#')?v:'#'+v;if(/^#[0-9A-Fa-f]{6}$/.test(c)){cur[k]=c;document.getElementById('pk'+sk).value=c;document.getElementById('sp'+sk).style.background=c;applyColors()}}
function onHB(k,sk,el){el.value=cur[k]||'#000000'}
function applyColors(){
  const r=document.documentElement;
  schema.forEach(i=>{const v=cur[i.k];if(!v)return;r.style.setProperty(i.k,v);const sk=i.k.replace(/-/g,'_');const sp=document.getElementById('sp'+sk);if(sp)sp.style.background=v;});
  updatePalette();updateContrast();
}
function applyPreset(name){
  if(!presets[name])return;cur={...presets[name]};
  schema.forEach(i=>{const sk=i.k.replace(/-/g,'_');const v=cur[i.k];if(!v)return;const pk=document.getElementById('pk'+sk);if(pk)pk.value=v;const hx=document.getElementById('hx'+sk);if(hx)hx.value=v;const sp=document.getElementById('sp'+sk);if(sp)sp.style.background=v;});
  applyColors();
}
function lum(h){if(!h||h.length<7)return 0;const r=parseInt(h.slice(1,3),16)/255,g=parseInt(h.slice(3,5),16)/255,b=parseInt(h.slice(5,7),16)/255;const l=c=>c<=0.03928?c/12.92:Math.pow((c+0.055)/1.055,2.4);return 0.2126*l(r)+0.7152*l(g)+0.0722*l(b)}
function cr(h1,h2){const l1=lum(h1),l2=lum(h2);const br=Math.max(l1,l2),dr=Math.min(l1,l2);return((br+.05)/(dr+.05)).toFixed(2)}
function updatePalette(){
  const g=document.getElementById('pg');
  const shown=['--primary','--secondary','--accent','--background','--surface','--error','--success','--warning'];
  g.innerHTML=shown.map(k=>{const hex=cur[k]||'#ccc';const lbl=schema.find(c=>c.k===k)?.l||k;const tc=lum(hex)>0.35?'#1A1A2E':'#FFFFFF';return`<div class="pc2" style="background:${hex};color:${tc}"><div class="pcl">${lbl}</div><div class="pch">${hex}</div></div>`;}).join('');
}
function updateContrast(){
  const c=document.getElementById('cc');
  const pairs=[{l:'Primary / On Primary',a:'--primary',b:'--text-on-primary'},{l:'Secondary / On Sec.',a:'--secondary',b:'--text-on-secondary'},{l:'BG / Text Primary',a:'--background',b:'--text-primary'},{l:'Surface / Text Sec.',a:'--surface',b:'--text-secondary'}];
  c.innerHTML=pairs.map(p=>{const ratio=cr(cur[p.a]||'#fff',cur[p.b]||'#000');const rv=parseFloat(ratio);const cls=rv>=4.5?'cpas':rv>=3?'cwrn':'cfal';const bd=rv>=4.5?'✓ '+ratio:rv>=3?'~ '+ratio:'✗ '+ratio;return`<div class="cr"><span class="crl">${p.l}</span><span class="crb ${cls}">${bd}</span></div>`;}).join('');
}
function parseVars(text){
  const map={};
  const re=/--([A-Za-z0-9-_]+)\s*:\s*([^;]+);/g;
  let m;
  while((m=re.exec(text))!==null){
    const k='--'+m[1];
    const v=m[2].trim();
    if(v)map[k]=v;
  }
  return map;
}
function importCSS(){
  const box=document.getElementById('importBox');
  if(!box)return;
  const vars=parseVars(box.value||'');
  const keys=Object.keys(vars);
  if(keys.length===0){showToast('No CSS variables found');return;}
  let matched=0;
  schema.forEach(i=>{if(vars[i.k]){cur[i.k]=vars[i.k];matched++;}});
  if(matched===0){showToast('No matching variables in schema');return;}
  schema.forEach(i=>{const sk=i.k.replace(/-/g,'_');const v=cur[i.k];if(!v)return;const pk=document.getElementById('pk'+sk);if(pk)pk.value=v;const hx=document.getElementById('hx'+sk);if(hx)hx.value=v;const sp=document.getElementById('sp'+sk);if(sp)sp.style.background=v;});
  applyColors();
  showToast(`Applied ${matched} variables`);
}
function exportCSS(){
  const lines=[':root {'];schema.forEach(i=>lines.push(`  ${i.k}: ${cur[i.k]||i.d};`));lines.push('}');
  navigator.clipboard.writeText(lines.join('\n')).then(()=>{showToast('✓ CSS variables copied!');});
}
buildControls();applyColors();
